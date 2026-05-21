pipeline {
  agent any

  tools {
    maven 'maven'
  }

  environment {
    APP_NAME = 'thymeleaf-crud'
    JAR_NAME = 'thymeleaf-crud-1.0.0.jar'
    DOCKERHUB_USER = 'deepthic18'
    DOCKERHUB_CREDENTIALS = 'deepthi123'
    BUILD_IMAGE = "${DOCKERHUB_USER}/${APP_NAME}:build-${BUILD_NUMBER}"
    SNAPSHOT_IMAGE = "${DOCKERHUB_USER}/${APP_NAME}:snapshot-${BUILD_NUMBER}"
    MASTER_CONTAINER = "${APP_NAME}-master"
    SNAPSHOT_TAR = "target/${APP_NAME}-snapshot-${BUILD_NUMBER}.tar"
    ANSIBLE_INVENTORY = 'ansible/inventory.ini'
    ANSIBLE_PLAYBOOK = 'ansible/deploy_docker_worker.yml'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build') {
      steps {
        script {
          if (isUnix()) {
            sh 'mvn clean package -DskipTests'
          } else {
            bat 'mvn clean package -DskipTests'
          }
        }
      }
    }

    stage('Test') {
      steps {
        script {
          if (isUnix()) {
            sh 'mvn test'
          } else {
            bat 'mvn test'
          }
        }
      }
    }

    stage('Docker Access Check') {
      steps {
        sh '''
          docker version >/dev/null 2>&1 || {
            echo "ERROR: Jenkins cannot access Docker."
            echo "Fix on Jenkins master: add the Jenkins runtime user to the docker group, then restart Jenkins."
            echo "Example: sudo usermod -aG docker jenkins && sudo systemctl restart jenkins"
            exit 1
          }
        '''
      }
    }

    stage('Docker Build') {
      steps {
        script {
          if (isUnix()) {
            sh "docker build -t ${BUILD_IMAGE} ."
          } else {
            error 'This Jenkins deployment pipeline requires a Linux master/agent with Docker and Ansible installed.'
          }
        }
      }
    }

    stage('Docker Hub Login') {
      steps {
        withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDENTIALS}", usernameVariable: 'DOCKERHUB_LOGIN_USER', passwordVariable: 'DOCKERHUB_LOGIN_PASSWORD')]) {
          sh 'echo "$DOCKERHUB_LOGIN_PASSWORD" | docker login -u "$DOCKERHUB_LOGIN_USER" --password-stdin'
        }
      }
    }

    stage('Run Container on Master') {
      steps {
        sh """
          docker rm -f ${MASTER_CONTAINER} || true
          docker run -d \\
            --name ${MASTER_CONTAINER} \\
            -p 8080:8080 \\
            -e SPRING_DATASOURCE_URL='jdbc:h2:mem:productdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE' \\
            -e SPRING_DATASOURCE_DRIVER_CLASS_NAME='org.h2.Driver' \\
            -e SPRING_DATASOURCE_USERNAME='sa' \\
            -e SPRING_DATASOURCE_PASSWORD='' \\
            -e SPRING_JPA_DATABASE_PLATFORM='org.hibernate.dialect.H2Dialect' \\
            -e SPRING_H2_CONSOLE_ENABLED='true' \\
            ${BUILD_IMAGE}
          sleep 20
          docker ps --filter "name=${MASTER_CONTAINER}" --filter "status=running" --format "{{.Names}}" | grep -w ${MASTER_CONTAINER}
        """
      }
    }

    stage('Snapshot Running Container') {
      steps {
        sh """
          docker commit ${MASTER_CONTAINER} ${SNAPSHOT_IMAGE}
          docker save ${SNAPSHOT_IMAGE} -o ${SNAPSHOT_TAR}
          ls -lh ${SNAPSHOT_TAR}
        """
      }
    }

    stage('Deploy Snapshot to Workers') {
      steps {
        script {
          sshagent(credentials: ['deepthi']) {
            sh """
              ansible-playbook -i ${ANSIBLE_INVENTORY} ${ANSIBLE_PLAYBOOK} \\
                --extra-vars "app_name=${APP_NAME} snapshot_image=${SNAPSHOT_IMAGE} snapshot_tar=${SNAPSHOT_TAR}"
            """
          }
        }
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: "target/${JAR_NAME},${SNAPSHOT_TAR}", fingerprint: true, allowEmptyArchive: true
    }
    failure {
      sh "docker version >/dev/null 2>&1 && docker logs --tail 100 ${MASTER_CONTAINER} || true"
    }
  }
}
