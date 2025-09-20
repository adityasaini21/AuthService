Expense Tracker API - JWT Authentication 🔐
A robust and secure backend authentication system for the Expense Tracker application, built using Java and Spring Boot. This project implements a modern authentication mechanism using JSON Web Tokens (JWT) with both Access Tokens and Refresh Tokens for enhanced security and user experience.

✨ Features
Secure User Registration: Endpoint for new users to sign up with hashed password storage (using BCrypt).

User Login: Authenticates users and returns a pair of JWTs.

Access Token: A short-lived token used to access protected resources.

Refresh Token: A long-lived token used to generate a new access token without requiring the user to log in again.

Token Refresh Endpoint: A dedicated endpoint (/api/auth/refresh) to get a new access token.

Protected Routes: Middleware to verify JWTs on incoming requests to secure application endpoints.

Centralized Exception Handling: Gracefully handles authentication and authorization errors.

⚙️ How It Works: The Authentication Flow
This system uses a token-based authentication flow which is ideal for stateless RESTful APIs.

Register & Login:

A new user registers via the /api/auth/register endpoint. Their password is securely hashed and stored.

The user logs in with their credentials at /api/auth/login.

Token Issuance:

Upon successful login, the server generates two tokens:

A short-lived Access Token (e.g., expires in 15 minutes).

A long-lived Refresh Token (e.g., expires in 7 days).

Both tokens are sent back to the client.

Accessing Protected Data:

To access a protected route (e.g., /api/expenses), the client sends the Access Token in the Authorization header of the request (Bearer <token>).

The server validates the token's signature and expiration. If valid, it grants access.

Refreshing the Session:

When the Access Token expires, the client will receive a 401 Unauthorized or 403 Forbidden error.

The client then calls the /api/auth/refresh endpoint, sending its Refresh Token.

The server validates the refresh token. If it's valid, it issues a brand new Access Token, allowing the user to continue their session seamlessly.

🛠️ Tech Stack
Java: 17

Framework: Spring Boot (3.1.4)

Security: Spring Security

JWT Library: jjwt (io.jsonwebtoken)

Database: MySQL with Spring Data JPA

Build Tool: Gradle

🚀 Getting Started
Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

Prerequisites
JDK 17 or later

Gradle

MySql running on your machine or in an Virtual Machine.

Installation
Clone the repository

Bash

git clone https://github.com/adityasaini21/AuthService.git
Navigate to the project directory

Bash

cd AuthService
Configure the application

Open the src/main/resources/application.properties file.

Update the database connection details (spring.datasource.url, username, password).

Set your own secret keys and expiration times for JWTs.

Install dependencies and build the project

Bash

gradle clean install
Run the application

Bash

gradle spring-boot:run
The application will start on http://localhost:9898.

🔧 Configuration
All major configurations are located in src/main/resources/application.properties. You should configure these properties, preferably using environment variables for production.

Properties

# Database Configuration
spring.datasource.url=[YOUR_DB_URL]
spring.datasource.username=[YOUR_DB_USERNAME]
spring.datasource.password=[YOUR_DB_PASSWORD]

# JWT Configuration
app.jwt.secret=[YOUR_SUPER_SECRET_KEY]
app.jwt.access-token-expiration-ms=900000 # 15 minutes
app.jwt.refresh-token-expiration-ms=604800000 # 7 days
🔑 API Endpoints
The following are the main endpoints exposed by this authentication service:

HTTP Method	Endpoint	Description	Authentication
POST	/api/auth/register	Registers a new user.	Public
POST	/api/auth/login	Logs in an existing user and returns tokens.	Public
POST	/api/auth/refresh	Issues a new access token using a valid refresh token.	Public
GET	/api/expenses	Example: Fetches user's expenses.	Protected
POST	/api/expenses	Example: Adds a new expense for the user.	Protected

Export to Sheets
📜 License
This project is licensed under the MIT License - see the LICENSE.md file for details.
