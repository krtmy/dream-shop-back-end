# Dream Shop Backend

A RESTful e-commerce backend built with Spring Boot.

## Features

- JWT Authentication
- Role-based Authorization (ADMIN / USER)
- Product Management
- Category Management
- Shopping Cart
- Order Management
- Image Upload
- Exception Handling

## Technologies

- Java 25
- Spring Boot 4
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- JWT
- Maven
- Lombok

## Project Structure

controller/
service/
repository/
model/
security/
config/
exception/
request/
response/

## Installation

1. Clone the repository.
2. Create a MySQL database named `shopcart_db`.
3. Configure environment variables:
    - `DB_USERNAME`
    - `DB_PASSWORD`
    - `JWT_SECRET`
4. Run the application.

## API Endpoints

- Authentication
- Users
- Products
- Categories
- Images
- Cart
- Orders

## Future Improvements

- Refresh Tokens
- Email Verification
- Docker
- Unit Tests
- Microservices

## Acknowledgements

This project was developed as part of my Spring Boot learning journey.

It is based on the **Dream Shops** tutorial series by **Daily Code Work**. During development, I adapted the project for newer Spring Boot versions, resolved compatibility issues, and made additional modifications while learning and practicing backend development.

I would like to thank **Daily Code Work** for creating the original educational content that inspired this project.