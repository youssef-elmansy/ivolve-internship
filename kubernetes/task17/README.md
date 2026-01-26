# Lab 17: Pod Resource Management with CPU and Memory Requests and Limits

## 🎯 Objective
Apply CPU and Memory resource requests and limits to an existing Node.js Deployment in Kubernetes and verify resource usage.

---

## 📦 Deployment (Full File After Update)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nodejs-app
spec:
  replicas: 2
  selector:
    matchLabels:
      app: nodejs
  template:
    metadata:
      labels:
        app: nodejs
    spec:
      imagePullSecrets:
      - name: dockerhub-secret

      tolerations:
      - key: "node"
        operator: "Equal"
        value: "worker"
        effect: "NoSchedule"

      initContainers:
      - name: init-mysql
        image: mysql:5.7
        env:
        - name: DB_HOST
          value: mysql.ivolve.svc.cluster.local
        - name: MYSQL_ROOT_USER
          value: root
        - name: MYSQL_ROOT_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-secret
              key: MYSQL_ROOT_PASSWORD
        - name: MYSQL_DATABASE
          valueFrom:
            secretKeyRef:
              name: mysql-secret
              key: MYSQL_DATABASE
        - name: MYSQL_USER
          valueFrom:
            secretKeyRef:
              name: mysql-secret
              key: MYSQL_USER
        - name: MYSQL_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-secret
              key: MYSQL_PASSWORD
        command:
        - sh
        - -c
        - |
          mysql -h $DB_HOST -u $MYSQL_ROOT_USER -p$MYSQL_ROOT_PASSWORD -e "
          CREATE DATABASE IF NOT EXISTS $MYSQL_DATABASE;
          CREATE USER IF NOT EXISTS '$MYSQL_USER'@'%' IDENTIFIED BY '$MYSQL_PASSWORD';
          GRANT ALL PRIVILEGES ON $MYSQL_DATABASE.* TO '$MYSQL_USER'@'%';
          FLUSH PRIVILEGES;"

      containers:   # 
      - name: nodejs
        image: youssefaelmansy/nodejs-iamge:1.0
        ports:
        - containerPort: 3000
        envFrom:
        - configMapRef:
            name: nodejs-config

        # Lab 17: Resources
        resources:
          requests:
            cpu: "1"
            memory: "1Gi"
          limits:
            cpu: "2"
            memory: "2Gi"

        volumeMounts:
        - name: app-storage
          mountPath: /usr/src/app/data

      volumes:
      - name: app-storage
        persistentVolumeClaim:
          claimName: app-pvc


```

---

## 🚀 Apply Deployment
```bash
kubectl apply -f deployment.yaml
```

## 🔍 Verification

### Check Applied Requests & Limits
```bash
kubectl describe pod <pod-name>
```

### Monitor Real-Time Resource Usage
```bash
kubectl top pod
```

---

## ✅ Expected Result
- Pods start successfully with defined resource constraints
- CPU and Memory usage visible using kubectl top
