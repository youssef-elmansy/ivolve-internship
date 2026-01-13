# Lab 4: Run Java Spring Boot App in a Container

This lab demonstrates how to containerize a Spring Boot Java application using a single-stage Dockerfile with a Java 17 base image.

All commands are written to be executed from the terminal using **Bash**.

---

## Prerequisites

- Linux / WSL environment
- Docker installed and running
- Git installed
- Maven installed (or use Maven wrapper) Docker installed and running

Verify Docker installation:
```bash
docker --version
```

---

## Step 1: Clone the Application Source Code

```bash
git clone https://github.com/Ibrahim-Adel15/Docker-1.git
cd Docker-1
```

---

## Step 2: Build the Application JAR

```bash
mvn clean package
```
Verify JAR file exists:

```bash 
ls -lh target/demo-0.0.1-SNAPSHOT.jar
```
## Step 3: Create Dockerfile

Create a file named `Dockerfile`:

```bash
vim Dockerfile
```

Dockerfile content:

```dockerfile
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY ./target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

```

---

## Step 4: Build Docker Image

```bash
docker build -t app1 .
```

Verify image creation:

```bash
docker images
```

---

## Step 5: Run Docker Container

```bash
docker run -d -p 8085:8080 --name container1 app_image
```

---

## Step 6: Test the Application

Using browser:

```text
http://localhost:8085
```

Or using curl:

```bash
curl http://localhost:8085
```

---

