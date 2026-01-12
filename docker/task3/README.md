# Lab3: Running a Java Spring Boot Application in Docker

This task demonstrates how to containerize and run a **Spring Boot Java application** using **Docker**.

All commands are written to be executed from the terminal using **Bash**.

---

## Objective

* Build a Spring Boot application inside a Docker image
* Run the application as a Docker container
* Expose and test the application service

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
FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /lab3

COPY ./Docker-1 .

RUN mvn package 

EXPOSE 8080

CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]

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

