# Lab 5: Multi-Stage Build for a Spring Boot App

This lab shows how to use multi-stage Docker builds to significantly reduce the final image size for a Spring Boot Java application.

All commands are written to be executed from the terminal using **Bash**.

---

## Prerequisites

* Linux / WSL environment
* Docker installed and running

Verify Docker installation:

```bash
docker --version
```

---

## Step 1: Clone the Application Source Code

```bash
git clone https://github.com/Ibrahim-Adel15/Docker-1.git
```

---

## Step 2: Create Dockerfile

Create a file named `Dockerfile`:

```bash
vim Dockerfile
```

Dockerfile content:

```dockerfile
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app


COPY ./Docker-1 .

RUN mvn package

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar ./app_using_multi_stage.jar

EXPOSE 8080

CMD ["java", "-jar", "app_using_multi_stage.jar"]
```

---

## Step 3: Build Docker Image

```bash
docker build -t app1 .
```

Verify image creation:

```bash
docker images
```

---

## Step 4: Run Docker Container

```bash
docker run -d -p 8085:8080 --name container1 app_image
```

---

## Step 5: Test the Application

Using browser:

```text
http://localhost:8085
```

Or using curl:

```bash
curl http://localhost:8085
```

---
