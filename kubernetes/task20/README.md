# Lab 20: Securing Kubernetes with RBAC and Service Accounts

## 🎯 Objective
Secure Kubernetes access using **RBAC** and **Service Accounts** by granting a Jenkins service account **full permissions on Pods** within a specific namespace.

---

## 📌 Requirements
- Kubernetes cluster up and running
- kubectl configured
- Namespace: `ivolve`

---

## Step 1: Create Namespace (if not exists)
```bash
kubectl create namespace ivolve
```

---

## Step 2: Create Service Account
Create a ServiceAccount named `jenkins-sa` in the `ivolve` namespace.

```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: jenkins-sa
  namespace: ivolve
```

Apply:
```bash
kubectl apply -f jenkins-sa.yaml
```

---

## Step 3: Create Role with Full Permissions on Pods
This Role grants **full access** to Pods (create, delete, update, list, etc.).

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: pod-full-access
  namespace: ivolve
rules:
- apiGroups: [""]
  resources: ["pods"]
  verbs: ["*"]
```

Apply:
```bash
kubectl apply -f pod-full-access-role.yaml
```

---

## Step 4: Bind Role to Service Account
Bind the Role to `jenkins-sa` using a RoleBinding.

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: pod-full-access-binding
  namespace: ivolve
subjects:
- kind: ServiceAccount
  name: jenkins-sa
  namespace: ivolve
roleRef:
  kind: Role
  name: pod-full-access
  apiGroup: rbac.authorization.k8s.io
```

Apply:
```bash
kubectl apply -f pod-full-access-binding.yaml
```

---

## Step 5: create Service Account Token using secret 

```bash
apiVersion: v1
kind: Secret
metadata:
  name: jenkins-sa-token
  namespace: ivolve
  annotations:
    kubernetes.io/service-account.name: jenkins-sa
type: kubernetes.io/service-account-token

Apply:
```bash
kubectl apply -f secret.yaml
```
---

## Step 6: Validation
Check permissions using `kubectl auth can-i`.

```bash
kubectl auth can-i get pods --as=system:serviceaccount:ivolve:jenkins-sa -n ivolve
```

```bash
kubectl auth can-i create pods --as=system:serviceaccount:ivolve:jenkins-sa -n ivolve
```

Expected output:
```
yes
```
<img width="1477" height="150" alt="20" src="https://github.com/user-attachments/assets/22618cd4-9c15-4c41-9dd7-8f088dfe0d3a" />


---

## ✅ Conclusion
- Jenkins ServiceAccount is securely configured
- RBAC enforces namespace-level access
- Full control over Pods granted only where required

---

## 🧠 Notes
- Always follow **least privilege** in production
- Prefer Roles over ClusterRoles for namespace isolation

