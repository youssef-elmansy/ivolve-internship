def call() {
    sh 'kubectl apply -f deployment.yaml -n jenkins'
}

