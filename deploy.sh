cd k8s
kubectl apply -f namespace.yaml
kubectl apply -f configmap.yaml
kubectl apply -f postgresql-deployment.yaml
kubectl apply -f zookeeper-deployment.yaml
kubectl apply -f kafka-deployment.yaml
kubectl apply -f kafka-ui-deployment.yaml
cd ..
podman build -t springboot-demo:latest .
cd k8s
kubectl apply -f app-deployment.yaml

