# Customer CRUD Application

## Overview

This project is a basic Customer CRUD (Create, Read, Update, Delete) application. It includes authentication using JWT (JSON Web Token) and performs CRUD operations on customer data. The project uses Java Servlets for the backend, MySQL for the database, and HTML/CSS/JavaScript for the frontend.

## Features

- User authentication with JWT.
- Create, read, update, and delete customer records.
- Fetch and display customer records in a paginated, sortable, and searchable table.
- Synchronize customer data with a remote API.

## Technologies Used

- **Backend**: Java Servlets, JSP
- **Frontend**: HTML, CSS, JavaScript
- **Database**: MySQL
- **Authentication**: JWT
- **Build Tool**: Maven
- **Server**: Apache Tomcat

## Prerequisites

- JDK 8 or later
- Apache Tomcat 9 or later
- MySQL
- Maven

## Setup Instructions

### Database Setup

1. Install MySQL and create a database for the application.
2. Run the following SQL script to create the `customers` table:

    ```sql
    CREATE DATABASE customerdb;

    USE customerdb;

    CREATE TABLE customers (
        id INT AUTO_INCREMENT PRIMARY KEY,
        first_name VARCHAR(50),
        last_name VARCHAR(50),
        street VARCHAR(100),
        address VARCHAR(100),
        city VARCHAR(50),
        state VARCHAR(50),
        email VARCHAR(100),
        phone VARCHAR(15)
    );
    ```

### Project Setup

1. Clone the repository:

    ```bash
    git clone https://github.com/your-username/customer-crud.git
    cd customer-crud
    ```

2. Open the project in Eclipse IDE.

3. Configure the MySQL database connection in `src/main/resources/db.properties`:

    ```properties
    db.url=jdbc:mysql://localhost:3306/customerdb
    db.username=root
    db.password=password
    ```

4. Update the `pom.xml` to include necessary dependencies:

    ```xml
    <dependencies>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.6</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.10.5</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.10.5</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.10.5</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.23</version>
        </dependency>
    </dependencies>
    ```

5. Build the project using Maven:

    ```bash
    mvn clean install
    ```

### Tomcat Setup

1. Add Apache Tomcat to Eclipse:
    - Go to `Window` > `Preferences` > `Server` > `Runtime Environments`.
    - Click `Add`, select `Apache Tomcat v9.0`, and click `Next`.
    - Browse to the Tomcat installation directory and click `Finish`.

2. Deploy the project to Tomcat:
    - Right-click on the project, select `Run As` > `Run on Server`.
    - Choose the configured Tomcat server and click `Finish`.

### Running the Application

1. Start the Tomcat server.
2. Open a web browser and navigate to `http://localhost:8080/customercrud/login.html`.
3. Log in with the credentials:
    - Username: `admin`
    - Password: `password`
4. After successful login, you will be redirected to the customer list page where you can perform CRUD operations.

### Remote API Synchronization

1. Click the "Sync" button on the customer list page to fetch and synchronize customer data from the remote API.
2. The application will authenticate with the remote API, fetch the customer list, and update the local database.
