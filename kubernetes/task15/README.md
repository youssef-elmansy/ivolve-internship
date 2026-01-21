# README.md - Lab 15: Node.js Application Deployment with ClusterIP Service (Private Image Supported)

## Objective

Deploy a Node.js Application on Kubernetes with:

* Deployment
* ConfigMap
* Static Persistent Volume
* Toleration
* ClusterIP Service
* Private Docker Hub Image

## Concepts Covered

* Deployment
* ConfigMap
* Persistent Volume (Static)
* Persistent Volume Claim
* Taints & Tolerations
* ClusterIP Service
* imagePullSecrets

## ConfigMap Example

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: nodejs-config
data:
  APP_PORT: "3000"
  APP_ENV: "production"
```

```bash
kubectl apply -f nodejs-config.yaml
```

## Secret for Private Docker Image

```bash
kubectl create secret docker-registry dockerhub-secret \
  --docker-server=https://index.docker.io/v1/ \
  --docker-username=DOCKERHUB_USERNAME \
  --docker-password=DOCKERHUB_PASSWORD \
  --docker-email=YOUR_EMAIL 
```

## PersistentVolumeClaim

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: app-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
```

```bash
kubectl apply -f app-pvc.yaml
```

## Deployment

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
      containers:
      - name: nodejs
        image: youssefaelmansy/nodejs-iamge:1.0
        ports:
        - containerPort: 3000
        envFrom:
        - configMapRef:
            name: nodejs-config
        volumeMounts:
        - name: app-storage
          mountPath: /usr/src/app/data
      volumes:
      - name: app-storage
        persistentVolumeClaim:
          claimName: app-pvc
```

```bash
kubectl apply -f nodejs-deployment.yaml
```

## ClusterIP Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: nodejs-service
spec:
  type: ClusterIP
  selector:
    app: nodejs
  ports:
  - port: 80
    targetPort: 3000
```

```bash
kubectl apply -f nodejs-service.yaml
```

## Verification

```bash
kubectl get pods
kubectl get deployment
kubectl get svc
kubectl get pvc
```

![verification](screenshots/1.png)

