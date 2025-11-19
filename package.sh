sudo ./mvnw clean package

sudo docker build -f src/main/docker/Dockerfile.jvm -t quarkus/hz_server-jvm .

sudo docker compose down
sudo docker compose up -d