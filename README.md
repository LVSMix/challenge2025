<p align="center">
  <a href="#"><img src="https://img.shields.io/badge/Spring_Boot-3.2.2-brightgreen" alt="Spring Boot"></a>
  <a href="#"><img src="https://img.shields.io/badge/chat-on%20Discord-7289da.svg?sanitize=true" alt="Chat"></a>
  <a href="#"><img src="https://img.shields.io/badge/Java-17-orange" alt="Chat"></a>
</p>

<br>
<br>
<p align="center">

# Challenge 2025 API

This project is built with Spring Boot. It provides API with diferent technologies like.

## Technologies Used

- **Java 21**
- **Spring Boot 3.3.4**
- **Maven**
- **SQL**
- **Docker**
- **Wiremock**
- **Redis**
- **Postgres SQL***

## Endpoints

### Get calculated result

- **URL:** `/api/calculator/calculate`
- **Method:** `GET`
- **Response:**
    - `200 OK`: percent obteined successfully.
    - `500 Internal Server Error`: An error occurred on the server.

### Get Logs

- **URL:** `/api/logs`
- **Method:** `GET`
- **Response:**
    - `200 OK`: logs retrieved successfully.
    - `500 Internal Server Error`: An error occurred on the server.


## 🔨 Build and Run

## Prerequisites

Ensure you have the following installed on your system:

- Java Development Kit (JDK 17)
- Maven


## Steps


### 1. Run docker compose

Run docker compose to start the Postgres and Redis services

```bash
docker-compose up
```
### 2. Clone the Repository

Clone your Spring Boot API repository to your local machine:

```bash
git clone https://github.com/LVSMix/challenge2025.git
```
```
cd challenge2025
```

### 3. Build the Project

Navigate to the root directory of your project and execute the following Maven command to build the project:
```
./mvnw clean install
```

### 4. Run the Application
Once the build is successful, you can run your Spring Boot application using the following command:
```
./mvnw spring-boot:run
```

### 5. Access the API
Your Spring Boot API should now be accessible at the default port 8080. Open your web browser or a tool like Postman and access the following URL:

> http://localhost:8081/swagger-ui/index.html#/

## Postman Collection

You can use the following Postman collection to test the API:

[Reto-Tempo.postman_collection.json](Reto-Tempo.postman_collection.json)

