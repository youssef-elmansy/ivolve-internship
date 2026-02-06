# Lab 23: CI/CD Pipeline Implementation with Jenkins Agents and Shared Libraries

## 🎯 Objective

In this lab, we will implement a CI/CD pipeline with Jenkins using agents and shared libraries to automate application deployment. The pipeline includes:

1. Run Unit Test
2. Build App
3. Build Docker Image
4. Scan Image
5. Push Image
6. Remove Image Locally
7. Deploy on Kubernetes

We will also demonstrate the usage of a shared library and configure a Jenkins agent to run the pipeline.

---

## 🛠 Prerequisites on the Agent VM

1. **Install Java** (check with `java -version`)  
2. **Required packages**: Maven, Docker, kubectl  
3. **Network access** to Jenkins Controller  
   - Port 50000 (JNLP) or port 22 (SSH)  
4. **SSH access** (if using SSH method)  

---

## 🔑 Step 1: Generate SSH Key (for SSH Agents)

On Jenkins Master VM:

```bash
ssh-keygen -t rsa -b 2048 -f ~/.ssh/jenkins_agent_key
ls ~/.ssh
```

Copy the public key to the Agent VM:

```bash
ssh-copy-id -i ~/.ssh/jenkins_agent_key.pub user@agent-ip
ssh -i ~/.ssh/jenkins_agent_key user@agent-ip
```

---

## 🏠 Step 2: Create Agent Home Directory

On Agent VM:

```bash
sudo mkdir /home/jenkins_home
sudo chmod 777 /home/jenkins_home
```

---

## 🔐 Step 3: Add SSH Credentials in Jenkins

1. Manage Jenkins → Credentials → Add  
2. Kind: **SSH Username with private key**  
3. Username: Agent VM user  
4. Private key: Paste the content of `jenkins_agent_key`  

---

## 🤖 Step 4: Create Jenkins Agent Node

1. Jenkins Dashboard → Manage Jenkins → Nodes → New Node  
2. Name: `linux-agent`  
3. Permanent Agent → OK  
4. Configure:
   - **# of Executors**: 1  
   - **Remote root directory**: `/home/jenkins_home`  
   - **Labels**: `agent-1`  
   - **Usage**: Use this node as much as possible  
   - **Launch method**: SSH or JNLP

---

### 🔹 Option A: SSH Agent

- Launch agents via SSH  
- Host: Agent VM IP  
- Credentials: SSH credentials  
- SSH Host Key Verification Strategy: Non-verifying

### 🔹 Option B: JNLP Agent

- On Agent VM:

```bash
mkdir /home/jenkins
cd /home/jenkins
wget http://<jenkins-host>:8080/jnlpJars/agent.jar
java -jar agent.jar -jnlpUrl http://<jenkins-host>:8080/computer/agent-vm-1/slave-agent.jnlp -secret <secret> -workDir "/home/jenkins"
```

---

## ✅ Step 5: Validate Agent

- Check the agent logs and ensure it’s **online** in Jenkins

---

## 📝 Step 6: Update Jenkinsfile to Use the Agent

If your node label is `agent-1`:

```groovy
pipeline {
    agent { label 'agent-1' }
    ...
}
```

---

## 📚 Step 7: Create Shared Library

### Folder Structure

```
vars/
├── buildApp.groovy
├── buildAndPushImage.groovy
└── deployToK8s.groovy
```

- Each file defines one function, filename = function name

---

## 🔗 Step 8: Add Shared Library in Jenkins

- Jenkins Dashboard → Manage Jenkins → System → Global Trusted Pipeline Libraries → Add
  - **Name**: `shared-lib`  
  - **Default version**: `main`  
  - **Retrieval method**: Modern SCM  
  - **Source Code Mgmt**: Git  
  - **Repository URL**: `<Shared Library Repo URL>`  
  - **Credentials**: optional (if private repo)

---

## 🧩 Step 9: Use Shared Library in Jenkinsfile

At the top of Jenkinsfile:

```groovy
@Library('shared-lib') _
```

Call functions:

```groovy
buildApp()
buildAndPushImage(IMAGE_NAME, env.BUILD_NUMBER)
deployToK8s(DEPLOYMENT_FILE, TOKEN_CREDENTIAL_ID, APISERVER_CREDENTIAL_ID)
```

---

## 📌 Summary

- Jenkins agents allow running pipelines on separate nodes  
- Shared libraries encapsulate common tasks for reuse  
- This lab demonstrates a full CI/CD pipeline with testing, building, image scanning, pushing, and deployment  
