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
docker tag todo-app:latest youracrname.azurecr.io/todo-app:latest
docker push youracrname.azurecr.io/todo-app:latest
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

## Next Steps
🔹 Implement **PostgreSQL** for production deployment.  
🔹 Deploy to **Azure Kubernetes Service (AKS)** with **Ingress Controller**.  
🔹 Add **CI/CD pipeline** for automated deployments.

---

### 🚀 *This README ensures that developers can quickly build, test, and deploy the backend service with minimal setup!* 🚀
