# Lab 10: Node Isolation Using Taints in Kubernetes

## 🎯 Objective
The purpose of this lab is to understand how Kubernetes Taints work and how they are used to isolate nodes and control pod scheduling.

---

## 🧠 Concepts Covered
- Kubernetes Nodes
- Taints
- NoSchedule effect
- Node isolation

---

## 🛠 Prerequisites
- Kubernetes cluster running
- kubectl configured
- Minikube installed

---

## 🚀 Steps

### 1️⃣ Start Kubernetes Cluster with 2 Nodes
```bash
minikube start --nodes=2
```

---

### 2️⃣ Verify Nodes
```bash
kubectl get nodes
```
![verify nodes](screenshots/nodes.png)

---

### 3️⃣ Apply Taint to Worker Node
```bash
kubectl taint node minikube-m02 node=worker:NoSchedule
```

---

### 4️⃣ Verify the Taint
```bash
kubectl describe node minikube-m02
```
![verify taint](taint.png)

---

## ✅ Result
- The node is successfully isolated.
- Pods without tolerations cannot be scheduled on the tainted node.

---

## 📌 Conclusion
Taints provide node-level control to restrict pod scheduling and are commonly used in production environments.

