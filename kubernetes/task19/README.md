# 🧪 Lab 19: Node-Wide Pod Management with DaemonSet

## 🎯 Objective

In this lab, we learn how to use a **DaemonSet** to run a Pod on **every Node** in the Kubernetes cluster. We will deploy **Prometheus Node Exporter** to collect node-level metrics.

---

## 🧩 Tasks

- Create a Namespace called `monitoring`
- Create a DaemonSet for Prometheus Node Exporter
- Verify that Pods are running on all Nodes
- Verify that metrics are exposed on port `9100`

---

## 📁 Step 1: Create Monitoring Namespace

**namespace.yaml**

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: monitoring
```

Apply the namespace:

```bash
kubectl apply -f namespace.yaml
```

---

## 📁 Step 2: Create DaemonSet for Prometheus Node Exporter

**daemonset.yaml**

```yaml
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: node-exporter
  namespace: monitoring
spec:
  selector:
    matchLabels:
      app: node-exporter
  template:
    metadata:
      labels:
        app: node-exporter
    spec:
      tolerations:
      - operator: "Exists"   # Allows scheduling on tainted nodes
      containers:
      - name: node-exporter
        image: prom/node-exporter:latest
        ports:
        - containerPort: 9100
          name: metrics
```

Apply the DaemonSet:

```bash
kubectl apply -f daemonset.yaml
```

---

## ✅ Validation

### 1️⃣ Verify Pod is Running on Every Node

```bash
kubectl get pods -n monitoring -o wide
```

✔️ You should see **one Pod per Node** in the cluster.

![verify pods](/screenshots/19-1.png)


---

### 🟢2️⃣  Solution 2: Verify Metrics from Inside the Cluster

Run a temporary test Pod:

```bash
kubectl run test --rm -it --image=busybox -- sh
```

Inside the Pod:

```bash
wget -qO- http://10.244.0.5:9100/metrics
```

✔️ This confirms that the Node Exporter is accessible internally within the cluster.


![verify metrics](/screenshots/19-2.png)


---

## 📌 Conclusion

- DaemonSet ensures that a Pod runs on **every Node**
- Prometheus Node Exporter exposes node-level metrics
- Tolerations allow scheduling on tainted nodes
- Metrics can be accessed both externally and internally

✅ Lab completed successfully

