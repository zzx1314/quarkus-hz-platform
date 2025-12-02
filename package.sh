#!/bin/bash

# 打包脚本 - 用于构建项目、创建Docker镜像并部署服务

set -e  # 遇到错误时退出

echo "开始打包流程..."

# 检查是否在root模式下运行
if [[ $EUID -eq 0 ]]; then
   echo "不建议以root用户运行此脚本"
   SUDO=""
else
   SUDO="sudo"
fi

# 默认选项
BUILD_IMAGE=true
RESTART_SERVICES=true
BUILD_NATIVE=false

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
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
            echo "选项:"
            echo "  --no-image    跳过构建Docker镜像"
            echo "  --no-restart  跳过重启服务"
            echo "  --native      构建原生镜像"
            echo "  -h, --help    显示帮助信息"
            exit 0
            ;;
        *)
            echo "未知选项: $1"
            echo "使用 -h 或 --help 查看可用选项"
            exit 1
            ;;
    esac
done

# 清理并打包项目
echo "1. 清理并打包项目..."
if $SUDO ./mvnw clean package -DskipTests; then
    echo "项目打包成功完成"
else
    echo "项目打包失败"
    exit 1
fi

# 构建Docker镜像
if [ "$BUILD_IMAGE" = true ]; then
    if [ "$BUILD_NATIVE" = true ]; then
        echo "2. 构建原生Docker镜像..."
        IMAGE_NAME="quarkus/hz_server-native"
        DOCKERFILE="src/main/docker/Dockerfile.native"
    else
        echo "2. 构建JVM Docker镜像..."
        IMAGE_NAME="quarkus/hz_server-jvm"
        DOCKERFILE="src/main/docker/Dockerfile.jvm"
    fi
    
    if $SUDO docker build -f $DOCKERFILE -t $IMAGE_NAME .; then
        echo "Docker镜像构建成功: $IMAGE_NAME"
    else
        echo "Docker镜像构建失败"
        exit 1
    fi
else
    echo "2. 跳过Docker镜像构建"
fi

# 重启服务
if [ "$RESTART_SERVICES" = true ]; then
    echo "3. 重启Docker Compose服务..."
    
    COMPOSE_FILE="src/main/docker/dockercompose/docker-compose.yml"
    if [ -f "$COMPOSE_FILE" ]; then
        # 停止并移除现有容器
        $SUDO docker compose -f $COMPOSE_FILE down
        
        # 启动新服务
        $SUDO docker compose -f $COMPOSE_FILE up -d
        
        echo "服务已重启完成"
    else
        echo "警告: Docker Compose文件不存在: $COMPOSE_FILE"
    fi
else
    echo "3. 跳过服务重启"
fi

echo "打包流程已完成!"