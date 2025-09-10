# Step 1: Stop all docker containers
docker compose stop data-provider-app stock-data-app heatmap-ui-app

# Step 2: Remove old containers if any
docker rm data-provider-app || true
docker rm stock-data-app || true
docker rm heatmap-ui-app || true

# Step 3: Maven install and docker build
cd project-data-provider
mvn clean install -DskipTests
docker build -t project-data-provider:0.0.1 .
cd ..

cd project-stock-data
mvn clean install -DskipTests
docker build -t project-stock-data:0.0.1 .
cd ..

cd project-heatmap-ui
mvn clean install -DskipTests
docker build -t project-heatmap-ui:0.0.1 .
cd ..

# Step 4: Docker run
docker compose up -d