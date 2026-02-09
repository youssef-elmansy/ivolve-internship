# Lab 24: Multi-Branch CI/CD Workflow with Jenkins & Kubernetes

## 🎯 Objective
Implement a **Multi-Branch CI/CD Pipeline** using Jenkins that deploys the same application to different Kubernetes namespaces based on the Git branch.

---

## 🧠 Lab Idea
- One project
- Three branches: `dev`, `stag`, `prod`
- Each branch deploys automatically to its own Kubernetes namespace
- Jenkins detects the branch name and deploys accordingly

---

## 📌 Branch → Namespace Mapping

| Git Branch | Kubernetes Namespace |
|----------|---------------------|
| dev      | dev                 |
| stag     | stag                |
| prod     | prod                |

---

## 🧪 Prerequisites

### Jenkins Agent
- Java
- Maven
- Docker
- kubectl
- Kubernetes access
- Agent label: `multi-branch-agent`

### Kubernetes Namespaces

```bash
kubectl create namespace dev
kubectl create namespace stag
kubectl create namespace prod
```

---

## 📂 Git Setup

```bash
git clone https://github.com/IbrahimAdel15/Jenkins_App.git
```

Create branches:

```bash
git checkout -b dev
git push origin dev

git checkout -b stag
git push origin stag

git checkout -b prod
git push origin prod
```

---

## 📚 Jenkins Shared Library
Pipeline uses shared functions:
- runUnitTest()
- buildApp()
- buildImage()
- scanImage()
- pushImage()
- deleteImage()

---

## 📝 Jenkinsfile

```groovy
@Library('shared-lib') _

def ns = ''

if (env.BRANCH_NAME == 'dev') {
    ns = 'dev'
} else if (env.BRANCH_NAME == 'stag') {
    ns = 'stag'
} else if (env.BRANCH_NAME == 'prod') {
    ns = 'prod'
}

pipeline {
    agent { label 'multi-branch-agent' }

    environment {
        IMAGE_NAME = "youssefaelmansy/java_app"
        NAMESPACE = ns
    }

    stages {
        stage('RunUnitTest') { steps { runUnitTest() } }
        stage('BuildApp') { steps { buildApp() } }
        stage('BuildImage') { steps { buildImage(IMAGE_NAME, BUILD_NUMBER) } }
        stage('ScanImage') { steps { scanImage(IMAGE_NAME, BUILD_NUMBER) } }
        stage('PushImage') { steps { pushImage(IMAGE_NAME, BUILD_NUMBER) } }
        stage('RemoveImageLocally') { steps { deleteImage(IMAGE_NAME, BUILD_NUMBER) } }
        stage('DeployOnK8s') {
            steps {
                sh "kubectl apply -f deployment.yaml -n ${NAMESPACE}"
            }
        }
    }
}
```

---

## 🔀 Jenkins Multibranch Pipeline
- Create **Multibranch Pipeline**
- Link GitHub repository
- Jenkins automatically detects branches
- Each branch deploys to its namespace

---

## ✅ Verification

```bash
kubectl get pods -n dev
kubectl get pods -n stag
kubectl get pods -n prod
```

---

## 🆚 Lab 23 vs Lab 24

| Lab 23 | Lab 24 |
|------|-------|
| Single pipeline | Multibranch pipeline |
| One namespace | Multiple namespaces |
| Manual deploy | Automatic by branch |

---

## 🎉 Conclusion
This lab simulates a real-world DevOps CI/CD workflow using Jenkins, Git branching, and Kubernetes namespaces.

