# Instructions: How to Build the Backend

This is the backend service for the **To-Do App**, built using **Spring Boot 3** and **Java 21**. It provides a secure REST API with **JWT authentication** and is designed to be **containerized** for deployment on **Azure Kubernetes Service (AKS) via Azure Container Registry (ACR).**

---

## Prerequisites

Before building the project, ensure you have:

- **Java 21** installed ([Download JDK 21](https://jdk.java.net/21/))
- **Maven** installed ([Maven Installation Guide](https://maven.apache.org/install.html))
- **Docker** installed and running ([Docker Installation Guide](https://docs.docker.com/get-docker/))

---

## Project Dependencies

The backend uses the following dependencies:

### ✅ **Spring Boot**
- `spring-boot-starter-web` – Web API development
- `spring-boot-starter-data-jpa` – Database interaction
- `spring-boot-starter-validation` – Input validation
- `spring-boot-starter-security` – Security and authentication

### ✅ **Database**
- `h2database` – Embedded database (for local testing)
- `postgresql` – PostgreSQL support (for production)

### ✅ **JWT Authentication**
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson` – JSON Web Token (JWT) support

### ✅ **OpenAPI / Swagger**
- `springdoc-openapi-starter-webmvc-ui` – API documentation

### ✅ **Utilities**
- `lombok` – Reduces boilerplate code

---

## Build Instructions

### 1️⃣ **Clone the repository**
```sh
git clone https://github.com/yourusername/todo-app.git
cd todo-app/backend
```

### 2️⃣ **Build the application using Maven**
```sh
./mvnw clean package
```
This generates a **JAR file** in the `target/` directory.

### 3️⃣ **Run the application locally**
```sh
java -jar target/todo-0.0.1-SNAPSHOT.jar
```
By default, the backend runs on **`http://localhost:8080`**.

---

## Running PostgreSQL with Docker

To run a PostgreSQL database in a Docker container:

```sh
docker run --name postgres-todo -e POSTGRES_USER=todo_user -e POSTGRES_PASSWORD=todo_pass -e POSTGRES_DB=todo_db -p 5432:5432 -d postgres:15
```

Check if the container is running:

```sh
docker ps
```

---

## **📂 Checking Database Tables in PostgreSQL**
To manually inspect the database, enter the **PostgreSQL CLI** inside the running container:

```sh
docker exec -it postgres-todo psql -U todo_user -d todo_db
```

Then, to list all tables, run:
```sql
\dt
```

### 📌 Example Console Output:
```plaintext
todo_db=# \dt
           List of relations
 Schema |   Name    | Type  |   Owner   
--------+-----------+-------+-----------
 public | audit_log | table | todo_user
 public | tasks     | table | todo_user
 public | users     | table | todo_user
(3 rows)

todo_db=# 
```

---

## Containerizing the Backend (Docker)

To deploy the backend as a **containerized microservice**, follow these steps:

### 1️⃣ **Build the Docker image**
```sh
docker build -t todo-app:latest .
```

### 2️⃣ **Run the Docker container**
```sh
docker run -p 8080:8080 todo-app:latest
```

The backend should now be accessible at **`http://localhost:8080`**.

---

## Deployment on Azure Kubernetes Service (AKS)

This backend is designed for **Azure Kubernetes Service (AKS)**. The deployment workflow involves:

### **1️⃣ Push to Azure Container Registry (ACR)**
```sh
docker tag todo-app:latest myacr.azurecr.io/todo-app:latest
docker push myacr.azurecr.io/todo-app:latest
```

### **2️⃣ Deploy to AKS**
```sh
kubectl apply -f k8s-deployment.yaml
kubectl apply -f k8s-service.yaml
```

The service will now be accessible via the **AKS Load Balancer**.

---

## API Documentation

Once the application is running, the **Swagger API documentation** is available at:
📌 **`http://localhost:8080/swagger-ui/index.html`**

---