# Lab 12: Managing Configuration and Sensitive Data with ConfigMaps and Secrets

## 🎯 Objective
This lab demonstrates how to manage application configuration and sensitive data securely using ConfigMaps and Secrets.

---

## 🧠 Concepts Covered
- ConfigMaps
- Secrets
- Base64 encoding
- Environment configuration

---

## 🛠 Prerequisites
- Kubernetes cluster
- kubectl configured
- Namespace ivolve created

---

## 🚀 Steps

## 🔹 Part 1: ConfigMap (Non-Sensitive Data)
```bash
kubectl create configmap mysql-config   --from-literal=DB_HOST=mysql.ivolve.svc.cluster.local   --from-literal=DB_USER=ivolve_user   -n ivolve   --dry-run=client -o yaml > mysql-config.yaml

kubectl apply -f mysql-config.yaml
```

---

## 🔐 Part 2: Secret (Sensitive Data)
```bash
kubectl create secret generic mysql-secret   --from-literal=DB_PASSWORD=ivolve123   --from-literal=MYSQL_ROOT_PASSWORD=root123   -n ivolve   --dry-run=client -o yaml > mysql-secret.yaml

kubectl apply -f mysql-secret.yaml
```

---

## 🔍 Verification
```bash
kubectl get configmap -n ivolve
kubectl get secret -n ivolve
```
[verify](/screenshots/verify.png)


---

## ✅ Result
- Configuration data is stored securely.
- Sensitive credentials are protected using Kubernetes Secrets.

---

## 📌 Conclusion
ConfigMaps and Secrets allow clean separation between application code and configuration.

