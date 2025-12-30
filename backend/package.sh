#!/bin/bash

# 打包脚本 - 用于构建项目、创建Docker镜像并部署服务
set -e

echo "开始打包流程..."

# 确保在脚本所在目录执行
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

# -----------------------------
# 默认选项
# -----------------------------
BUILD_BACKEND=true
BUILD_FRONTEND=true
BUILD_IMAGE=true
RESTART_SERVICES=true
BUILD_NATIVE=false

# -----------------------------
# 参数解析
# -----------------------------
while [[ $# -gt 0 ]]; do
  case "$1" in
    --no-backend)
      BUILD_BACKEND=false
      shift
      ;;
    --no-frontend)
      BUILD_FRONTEND=false
      shift
      ;;
    --no-image)
      BUILD_IMAGE=false
      shift
      ;;
    --no-restart)
      RESTART_SERVICES=false
      shift
      ;;
    --native)
      BUILD_NATIVE=true
      shift
      ;;
    -h|--help)
      echo "用法: $0 [选项]"
      echo ""
      echo "选项:"
      echo "  --no-backend   跳过后端构建（mvn / docker / compose）"
      echo "  --no-frontend  跳过前端构建"
      echo "  --no-image     跳过 Docker 镜像构建"
      echo "  --no-restart   跳过 Docker Compose 重启"
      echo "  --native       构建原生镜像"
      echo "  -h, --help     显示帮助信息"
      exit 0
      ;;
    *)
      echo "❌ 未知选项: $1"
      exit 1
      ;;
  esac
done

# -----------------------------
# 参数合法性校验
# -----------------------------
if [ "$BUILD_BACKEND" = false ] && [ "$BUILD_FRONTEND" = false ]; then
  echo "❌ backend 和 frontend 都被禁用，没有任何构建内容"
  exit 1
fi

# =========================================================
# Backend 构建
# =========================================================
if [ "$BUILD_BACKEND" = true ]; then
  echo "1. 清理并打包 Backend 项目..."

  # ❗ Maven 永远不要 sudo
  ./mvnw clean package -DskipTests
  echo "✅ Backend 打包完成"
else
  echo "1. 跳过 Backend 构建"
fi

# =========================================================
#  Docker 镜像
# =========================================================
if [ "$BUILD_BACKEND" = true ] && [ "$BUILD_IMAGE" = true ]; then
  if [ "$BUILD_NATIVE" = true ]; then
    echo "2. 构建 Native Docker 镜像..."
    IMAGE_NAME="quarkus/hz_server-native"
    DOCKERFILE="src/main/docker/Dockerfile.native"
  else
    echo "2. 构建 JVM Docker 镜像..."
    IMAGE_NAME="quarkus/hz_server-jvm"
    DOCKERFILE="src/main/docker/Dockerfile.jvm"
  fi

  docker build -f "$DOCKERFILE" -t "$IMAGE_NAME" .
  echo "✅ Docker 镜像构建完成: $IMAGE_NAME"
else
  echo "2. 跳过 Docker 镜像构建"
fi


# =========================================================
# Docker 镜像导出 & 传输 & 远程加载 & 重启
# =========================================================
if [ "$BUILD_BACKEND" = true ] && [ "$BUILD_IMAGE" = true ]; then
  IMAGE_TAR="hz-server.tar"
  REMOTE_USER="root"
  REMOTE_HOST="192.168.41.225"
  REMOTE_PATH="/root/dockerimages"

  echo "3. 导出 Docker 镜像到 $IMAGE_TAR..."
  docker save -o "$IMAGE_TAR" "$IMAGE_NAME"
  echo "✅ 镜像导出完成: $IMAGE_TAR"

  echo "4. 使用 scp 传输到远程服务器 $REMOTE_HOST:$REMOTE_PATH ..."
  scp "$IMAGE_TAR" "${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}"
  echo "✅ 镜像已传输完成"

  echo "5. 删除本地镜像文件 $IMAGE_TAR ..."
  rm -rf "$IMAGE_TAR"

  echo "6. 在远程服务器加载镜像并重启服务..."
  ssh "${REMOTE_USER}@${REMOTE_HOST}" bash -c "'
    set -e
    cd ${REMOTE_PATH}

    echo \"1. 加载 Docker 镜像 ${IMAGE_TAR}...\"
    docker load -i ${IMAGE_TAR}

    echo \"2. 查看 dangling 镜像...\"
    docker images -f dangling=true

    echo \"3. 清理 dangling 镜像...\"
    docker image prune -f

    echo \"4. 重启 Docker Compose 服务...\"
    cd /root/dockercompose/drones
    docker-compose down
    docker-compose up -d

    echo \"✅ 远程服务器操作完成\"
  '"
else
  echo "3. 跳过 Docker 镜像导出、传输及远程操作"
fi

# =========================================================
# Docker Compose
# =========================================================
if [ "$BUILD_BACKEND" = true ] && [ "$RESTART_SERVICES" = true ]; then
  echo "3. 重启 Docker Compose 服务..."

  COMPOSE_FILE="src/main/docker/dockercompose/docker-compose.yml"

  if [ -f "$COMPOSE_FILE" ]; then
    docker compose -f "$COMPOSE_FILE" down
    docker compose -f "$COMPOSE_FILE" up -d
    echo "✅ 服务重启完成"
  else
    echo "⚠️ Docker Compose 文件不存在: $COMPOSE_FILE"
  fi
else
  echo "3. 跳过服务重启"
fi

# =========================================================
# Frontend 构建
# =========================================================
if [ "$BUILD_FRONTEND" = true ]; then
  echo "4. 构建 Frontend 项目..."

  FRONTEND_PROJECT="../frontend"

  if [ -d "$FRONTEND_PROJECT" ] && [ -f "$FRONTEND_PROJECT/build.sh" ]; then
    pushd "$FRONTEND_PROJECT" >/dev/null
    ./build.sh
    popd >/dev/null
    echo "✅ Frontend 构建完成"
  else
    echo "⚠️ 前端项目或 build.sh 不存在: $FRONTEND_PROJECT"
  fi
else
  echo "4. 跳过 Frontend 构建"
fi

echo "🎉 完整的打包流程已完成!"
