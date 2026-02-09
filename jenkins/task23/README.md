# Lab 23: CI/CD Pipeline Implementation with Jenkins Agents and Shared Libraries

## 🎯 Objective

Implement a Jenkins pipeline using **Agents** and **Shared Libraries** to automate:

1. Run Unit Test
2. Build Application
3. Build Docker Image
4. Scan Docker Image
5. Push Docker Image
6. Remove Local Docker Image
7. Deploy Application to Kubernetes

---

## 🧪 Prerequisites on the Agent VM

* Java installed (`java -version` must work)
* Maven (`mvn --version`)
* Docker (`docker ps`)
* kubectl (`kubectl version`)
* Network access to Jenkins controller
* SSH access if using SSH connection

---

## 🔑 Setup Jenkins Agent

### 1. Generate SSH Key on Jenkins Master

```bash
ssh-keygen -t rsa -b 2048 -f ~/.ssh/jenkins_agent_key
ssh-copy-id -i ~/.ssh/jenkins_agent_key.pub user@agent-ip
ssh -i ~/.ssh/jenkins_agent_key user@agent-ip
```

### 2. Create Agent Home Directory

```bash
sudo mkdir /home/jenkins_home
sudo chmod 777 /home/jenkins_home
```

### 3. Add SSH Credentials in Jenkins

* Manage Jenkins → Credentials → Add
* Kind: SSH Username with private key
* Username: Agent VM user
* Private Key: Paste the private key

### 4. Add Jenkins Node

* Dashboard → Manage Jenkins → Nodes → New Node
* Name: `linux-agent`
* Permanent Agent, Remote root directory: `/home/jenkins_home`
* Labels: `agent-1`
* Launch method: SSH or JNLP

---

## 📂 Create Shared Library

### Folder Structure

```
vars/
├── buildApp.groovy
├── buildImage.groovy
├── deleteImage.groovy
├── deployOnK8s.groovy
├── pushImage.groovy
├── runUnitTest.groovy
└── scanImage.groovy
```

* Each file defines a function accessible in the Jenkinsfile.

### Add Library to Jenkins

* Manage Jenkins → System → Global Pipeline Libraries → Add
* Name: `shared-lib`
* Default version: `main`
* SCM: Git (add repo URL)

---

## 📝 Jenkinsfile Example

```groovy
@Library('shared-lib') _

pipeline {
    agent { label 'agent-1' }

    environment {
        IMAGE_NAME = "youssefaelmansy/java_app"
    }

    stages {

        stage('RunUnitTest') {
            steps {
                runUnitTest()
            }
        }

        stage('BuildApp') {
            steps {
                buildApp()
            }
        }

        stage('BuildImage') {
            steps {
                buildImage(IMAGE_NAME, BUILD_NUMBER)
            }
        }

        stage('ScanImage') {
            steps {
                scanImage(IMAGE_NAME, BUILD_NUMBER)
            }
        }

        stage('PushImage') {
            steps {
                pushImage(IMAGE_NAME, BUILD_NUMBER)
            }
        }

        stage('RemoveImageLocally') {
            steps {
                deleteImage(IMAGE_NAME, BUILD_NUMBER)
            }
        }

        stage('DeployOnK8s') {
            steps {
                deployOnK8s()
            }
        }
    }

    post {
        always {
            echo "Pipeline finished!"
        }
        success {
            echo "Deployment successful!"
        }
        failure {
            echo "Pipeline failed. Check logs!"
        }
    }
}
```

---

## ✅ Validation

1. Check the pipeline runs successfully on the Agent node.
2. Confirm Docker image is pushed to Docker Hub.
3. Check Kubernetes deployment is updated with the new image.
4. Validate application is running in the cluster.
<img width="1920" height="770" alt="image" src="https://github.com/user-attachments/assets/7c02ff37-fc75-4012-8fe3-b77aaaeb7224" />
