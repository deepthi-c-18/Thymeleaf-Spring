pipeline {
  agent any

  environment {
    APP_NAME = 'thymeleaf-crud'
    JAR_NAME = 'thymeleaf-crud-1.0.0.jar'
    IMAGE_NAME = 'your-dockerhub-username/thymeleaf-crud:latest'
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

    stage('Docker Build') {
      steps {
        script {
          if (isUnix()) {
            sh "docker build -t ${IMAGE_NAME} ."
          } else {
            bat "docker build -t %IMAGE_NAME% ."
          }
        }
      }
    }

    stage('Docker Push') {
      steps {
        script {
          if (isUnix()) {
            sh "docker push ${IMAGE_NAME}"
          } else {
            bat "docker push %IMAGE_NAME%"
          }
        }
      }
    }

    stage('Deploy') {
      steps {
        script {
          def command = "ansible-playbook -i ${ANSIBLE_INVENTORY} ${ANSIBLE_PLAYBOOK} --extra-vars 'app_name=${APP_NAME} docker_image=${IMAGE_NAME}'"
          if (isUnix()) {
            sh command
          } else {
            bat command
          }
        }
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: "target/${JAR_NAME}", fingerprint: true
    }
  }
}
