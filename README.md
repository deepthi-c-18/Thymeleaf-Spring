# Thymeleaf Spring Boot CRUD Application

A modern, fully-featured Product Management System built with Spring Boot, Thymeleaf, and Bootstrap 5. This application demonstrates best practices for building web applications with a beautiful user interface.

## Features

✅ **Complete CRUD Operations** - Create, Read, Update, and Delete products  
✅ **Beautiful UI** - Bootstrap 5 with custom CSS for a professional look  
✅ **Form Validation** - Client-side and server-side validation  
✅ **Search & Filter** - Search products by name and filter by category  
✅ **Stock Management** - Track inventory levels with low stock alerts  
✅ **Responsive Design** - Works seamlessly on desktop, tablet, and mobile devices  
✅ **Data Persistence** - H2 in-memory database (easily configurable to any database)  
✅ **Clean Architecture** - Separation of concerns with Controller, Service, and Repository layers  
✅ **Thymeleaf Templates** - Server-side template rendering with Bootstrap integration  

## Technology Stack

- **Backend**: Spring Boot 3.2.0
- **Java Version**: 17
- **ORM**: Spring Data JPA with Hibernate
- **Database**: H2 (in-memory database for demo)
- **Frontend**: Thymeleaf, Bootstrap 5, Bootstrap Icons
- **Build Tool**: Maven
- **Additional**: Lombok, Validation API

## Project Structure

```
thymeleaf-crud/
├── src/
│   ├── main/
│   │   ├── java/com/example/thymeleaf/
│   │   │   ├── ThymeleafCrudApplication.java
│   │   │   ├── controller/
│   │   │   │   └── ProductController.java
│   │   │   ├── entity/
│   │   │   │   └── Product.java
│   │   │   ├── repository/
│   │   │   │   └── ProductRepository.java
│   │   │   └── service/
│   │   │       └── ProductService.java
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── products/
│   │       │   │   ├── list.html
│   │       │   │   ├── form.html
│   │       │   │   └── view.html
│   │       │   └── layout.html
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css
│   │       │   └── js/
│   │       │       └── script.js
│   │       └── application.properties
│   └── test/
│       └── java/...
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git

### Installation

1. **Clone or navigate to the project directory:**
   ```bash
   cd "thymleaf project"
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application:**
   Open your browser and navigate to:
   ```
   http://localhost:8080/products
   ```

5. **Access H2 Database Console (optional):**
   ```
   http://localhost:8080/h2-console
   ```
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave empty)

## API Endpoints

### Products Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/products` | List all products with search and filter |
| GET | `/products/new` | Show create product form |
| POST | `/products` | Create a new product |
| GET | `/products/{id}` | View product details |
| GET | `/products/{id}/edit` | Show edit product form |
| POST | `/products/{id}` | Update a product |
| POST | `/products/{id}/delete` | Delete a product |

## Product Entity

The `Product` entity includes the following fields:

- **id** - Auto-generated unique identifier
- **name** - Product name (3-100 characters)
- **sku** - Stock Keeping Unit (unique, max 50 characters)
- **description** - Detailed product description (10-500 characters)
- **price** - Product price (decimal, 2 decimal places)
- **category** - Product category (2-50 characters)
- **stockQuantity** - Number of units in stock (non-negative)
- **createdAt** - Timestamp of product creation
- **updatedAt** - Timestamp of last update

## Features in Detail

### 1. Product Listing
- View all products in an attractive table format
- Real-time search functionality
- Filter by product category
- Sort products
- Stock status indicators

### 2. Create Product
- User-friendly form with validation
- Real-time field validation feedback
- Auto-populated category suggestions
- Price and quantity formatting

### 3. View Product Details
- Complete product information display
- Stock status and availability
- Creation and update timestamps
- Quick edit and delete options

### 4. Edit Product
- Pre-filled form with current product data
- Same validation rules as create
- Unique SKU validation
- Change tracking with updated timestamp

### 5. Delete Product
- Confirmation modal before deletion
- Prevents accidental deletions
- Feedback on successful deletion

### 6. Search & Filter
- Real-time product search by name
- Filter products by category
- Combined search functionality

## Validation Rules

- **Product Name**: 3-100 characters, required
- **SKU**: Unique, required, max 50 characters
- **Description**: 10-500 characters, required
- **Price**: 0.01-999999.99, required
- **Category**: 2-50 characters, required
- **Stock Quantity**: Non-negative integer

## Configuration

### Database Configuration

To switch from H2 to a different database, update `application.properties`:

#### PostgreSQL Example:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/productdb
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

#### MySQL Example:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/productdb
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

### Application Properties

Key configuration options in `application.properties`:

```properties
spring.application.name=thymeleaf-crud
spring.jpa.hibernate.ddl-auto=update  # Auto-create/update tables
spring.jpa.show-sql=true              # Log SQL queries
server.port=8080                      # Server port
server.servlet.context-path=/         # Context path
```

## Adding Dependencies

To add new dependencies, edit `pom.xml` and run:
```bash
mvn clean install
```

## Development Tools

### Spring Boot DevTools
Enabled by default for auto-restart on file changes.

### H2 Database Console
Access at `/h2-console` to inspect the database during development.

### Lombok
Reduces boilerplate code by auto-generating getters, setters, constructors, etc.

## Best Practices Implemented

1. **Separation of Concerns**
   - Controllers handle HTTP requests
   - Services contain business logic
   - Repositories handle data access

2. **Validation**
   - Server-side validation using Jakarta Validation
   - Client-side validation in HTML5
   - Custom error messages

3. **Exception Handling**
   - Meaningful error messages
   - User-friendly error displays

4. **Code Organization**
   - Clear folder structure
   - Logical naming conventions
   - Single responsibility principle

5. **UI/UX**
   - Responsive design
   - Intuitive navigation
   - Clear visual hierarchy
   - Accessibility features

## Future Enhancements

- [ ] User authentication and authorization
- [ ] Pagination for large product lists
- [ ] Sorting capabilities
- [ ] Product image uploads
- [ ] Bulk operations (import/export)
- [ ] Product reviews and ratings
- [ ] Advanced analytics and reporting
- [ ] REST API endpoints
- [ ] Integration tests
- [ ] API documentation with Swagger

## Troubleshooting

### Port 8080 is already in use
Change the port in `application.properties`:
```properties
server.port=8081
```

### H2 Database is locked
Restart the application. H2 in-memory database resets on each restart.

### Validation errors not showing
Ensure form has `th:object="${product}"` and fields use `th:field="*{fieldName}"`.

### Thymeleaf templates not found
Check that template files are in `src/main/resources/templates/` directory.

## License

This project is open source and available for educational and commercial use.

## Author

Created as a demonstration of Spring Boot + Thymeleaf best practices.

## Support

For issues, questions, or suggestions, please refer to the code comments and Spring Boot documentation.

---

**Happy Coding! 🚀**
