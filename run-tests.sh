#!/usr/bin/env bash

set -e

echo "========================================================"
echo "🚀 1. Triển khai Docker Compose cho StudyDocs Backend"
echo "========================================================"
docker compose down -v --remove-orphans || true
docker compose up -d --build

echo "========================================================"
echo "⏳ 2. Đang chờ Backend và MySQL khởi chạy và sẵn sàng..."
echo "========================================================"
until curl -s http://localhost:8090/api/v1/actuator/health | grep -q '"status":"UP"' || curl -s http://localhost:8090/api/v1/education/academics/universities | grep -q '"statusCode":200'; do
    echo "Wait for backend application health..."
    sleep 3
done

echo "========================================================"
echo "🧪 3. Khởi chạy Postman API Collection Tests via Newman"
echo "========================================================"
npx --yes newman run postman/StudyDocs_Backend_API.postman_collection.json \
  --env-var "baseUrl=http://localhost:8090/api/v1" \
  --reporters cli

echo "========================================================"
echo "✅ TOÀN BỘ KIỂM THỬ POSTMAN TRÊN DOCKER COMPOSE ĐÃ HOÀN THÀNH XUẤT SẮC!"
echo "========================================================"
