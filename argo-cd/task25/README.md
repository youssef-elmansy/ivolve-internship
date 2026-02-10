# Task 25 – GitOps Workflow (Jenkins + Argo CD)

## 📌 Overview
This task demonstrates a complete **GitOps workflow** using **Jenkins (CI)** and **Argo CD (CD)** on a Kubernetes cluster.

Jenkins is responsible for CI (build, image, push),
while Argo CD handles CD by syncing Kubernetes manifests from Git.

---

## 🧱 Architecture

Application Repo  
→ Jenkins (CI)  
→ Docker Hub  
→ GitOps Repo (K8s Manifests)  
→ Argo CD  
→ Kubernetes Cluster  

---

## 🛠 Prerequisites
- Kubernetes cluster
- kubectl configured
- Jenkins installed
- Docker installed
- Docker Hub account
- GitHub account
- Argo CD installed

---

## 1️⃣ Install Argo CD

```bash
kubectl create namespace argocd

kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```

Expose Argo CD UI:
```bash
kubectl port-forward svc/argocd-server -n argocd 8080:443
```

---

## 2️⃣ GitOps Repository Structure

```
argocd-repo-task25/
    ├── deployment.yaml

---

## 3️⃣ Kubernetes Deployment Manifest

File: `deployment.yaml`

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: app-deployment
  namespace: jenkins
  labels:
    app: app
spec:
  replicas: 1
  selector:
    matchLabels:
      app: app
  template:
    metadata:
      labels:
        app: app
    spec:
      imagePullSecrets:
        - name: jenkins-dockerhub-secret
      containers:
        - name: app-container
          image: youssefaelmansy/java_app:latest
          ports:
            - containerPort: 8080
          imagePullPolicy: Always
```

🔹 **Explanation**
- `namespace: jenkins` → deployment runs inside Jenkins namespace
- `imagePullSecrets` → used to pull private image from Docker Hub
- `image` → updated automatically by Jenkins pipeline
- `imagePullPolicy: Always` → ensures latest image is pulled

---

## 4️⃣ Jenkins CI Pipeline Flow

Jenkins pipeline automates:
1. Build application
2. Build Docker image
3. Push image to Docker Hub
4. Delete image locally
5. Update image tag in `deployment.yaml`
6. Push updated manifest to GitOps repository

Once Git is updated, Argo CD detects the change and deploys automatically.

---

## 5️⃣ Argo CD Behavior (GitOps)

- Git is the **single source of truth**
- Any **manual change in Kubernetes** will be reverted
- Sync happens automatically when Git changes
- Self-heal keeps cluster aligned with Git state

---

## ✅ Final Result

- Jenkins handles **CI only**
- Argo CD handles **CD only**
- Clean separation of responsibilities
- Production-ready GitOps workflow

---

## ✨ Conclusion

This lab implements a real-world **GitOps CI/CD pipeline**
used in modern DevOps environments.

