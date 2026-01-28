# README: Jenkins Setup & Lab 21 RBAC

## Objective
Set up Jenkins using Docker and complete Lab 21: Role Based Authorization (RBAC).

---

## 1. Prerequisites
- Ubuntu VM (tested on 24.04)
- Docker installed
- Internet connection

---

## 2. Run Jenkins via Docker

### Step 1: Pull Jenkins LTS Docker image
```bash
docker pull jenkins/jenkins:lts
```

### Step 2: Run Jenkins container
```bash
docker run -d \
  --name jenkins \
  -p 8080:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  jenkins/jenkins:lts
```

### Step 3: Check container status
```bash
docker ps
```
Expected output:
```
CONTAINER ID   IMAGE                 COMMAND   STATUS   PORTS   NAMES
...
```

### Step 4: Get initial admin password
```bash
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```
Use this password to unlock Jenkins via browser: `http://<VM-IP>:8080`

### Step 5: Install suggested plugins and create admin user
Follow the Jenkins GUI wizard to install plugins and create an admin account.

---

## 3. Lab 21: Role Based Authorization (RBAC)

### Step 1: Create Users
1. Go to `Manage Jenkins` → `Manage Users` → `Create User`
2. Create `user1` and `user2` with passwords.

### Step 2: Configure Roles
1. Go to `Manage Jenkins` → `Configure Global Security`
2. Under `Authorization`, select `Matrix-based security`
3. Assign permissions:
   - `user1`: Full Admin Permissions
   - `user2`: Read-only Permissions
4. Save configuration.



### Step 3: Verify
- Log in as `user1` → verify admin access
- Log in as `user2` → verify read-only access

---

## 4. Notes
- Docker container persists Jenkins data in the named volume `jenkins_home`
- Mounting Docker socket allows Jenkins to build Docker images inside pipelines
- Any VM restart requires starting Docker and Jenkins container:
```bash
sudo systemctl start docker
sudo systemctl enable docker
docker start jenkins
docker ps
```
- Jenkins will retain all configurations, users, and plugins thanks to the mounted volume

---

## 5. Troubleshooting
- If Jenkins container is stopped: `docker start jenkins`
- If Docker daemon is not running: `sudo systemctl start docker`
- Access GUI at `http://<VM
