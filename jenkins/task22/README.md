# Lab 22: Jenkins Pipeline for Application Deployment

## 🎯 Objective
Automate the deployment of a Java application on Kubernetes using a Jenkins Pipeline.

This pipeline performs:
1. Unit Test
2. Build App (Maven)
3. Build Docker Image
4. Push Image to Docker Hub
5. Delete Local Docker Image
6. Update deployment.yaml
7. Deploy to Kubernetes Cluster

---

## 🛠️ Prerequisites
- Jenkins server with Docker installed
- Kubernetes cluster configured
- DockerHub account and credentials stored in Jenkins (`DockerHub` credential ID)
- Namespace `jenkins` exists in the cluster

---

## 📂 Repository Structure

```
task22-repo/
│
├── Jenkinsfile          # Pipeline definition
├── Dockerfile           # Docker build instructions
├── deployment.yaml      # Kubernetes deployment
└── src/...              # Java source code
```

---

## 🧩 Pipeline Stages

### 1️⃣ Unit Test
Runs Maven tests inside the repo.

```bash
mvn test
```

### 2️⃣ Build App
Builds the JAR package:

```bash
mvn clean package
```

### 3️⃣ Build Docker Image
Builds Docker image using the Dockerfile:

```bash
docker build -t youssefaelmansy/java_app:$BUILD_NUMBER .
```

### 4️⃣ Push to Docker Hub
Logs in using Jenkins stored credentials and pushes the image:

```bash
docker login -u $USER --password-stdin
docker push youssefaelmansy/java_app:$BUILD_NUMBER
```

### 5️⃣ Delete Local Docker Image
Removes the local image to save space:

```bash
docker rmi youssefaelmansy/java_app:$BUILD_NUMBER || true
```

### 6️⃣ Update deployment.yaml
Updates the deployment file with the new image tag:

```bash
sed -i 's|image:.*|image: youssefaelmansy/java_app:$BUILD_NUMBER|' deployment.yaml
```

### 7️⃣ Deploy to Kubernetes
Applies the updated deployment to the `jenkins` namespace:

```bash
kubectl apply -f deployment.yaml -n jenkins
```

---

## ⚡ Post Actions
The pipeline includes post actions to handle pipeline status:

- `always`: Display message that pipeline finished
- `success`: Display deployment success message
- `failure`: Display failure message

---
<img width="1917" height="777" alt="image" src="https://github.com/user-attachments/assets/9ced7af1-73b6-4e3b-8314-6ad0e88887c7" />

## ✅ Verification

1. Check pods in `jenkins` namespace:

```bash
kubectl get pods -n jenkins
```
<img width="766" height="67" alt="image" src="https://github.com/user-attachments/assets/705024e3-6e9e-4555-ba1c-51e90d2c8d73" />

2. Verify deployment:

```bash
kubectl get deployment app-deployment -n jenkins
```
<img width="817" height="67" alt="image" src="https://github.com/user-attachments/assets/3d932fff-df4e-4e97-9f9d-238414f6a925" />

---

## 📌 Notes

- Make sure Jenkins agent has Docker installed and can run `kubectl`.
- Ensure DockerHub credentials are configured correctly in Jenkins.
- Namespace `jenkins` should exist beforehand.
- Pipeline uses `$BUILD_NUMBER` to version Docker images automatically.
