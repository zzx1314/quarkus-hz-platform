#!/bin/bash

# 设置脚本在遇到错误时立即退出
set -e

# 定义颜色输出函数
info() { echo -e "\033[36m[INFO]\033[0m $1"; }
success() { echo -e "\033[32m[SUCCESS]\033[0m $1"; }
error() { echo -e "\033[31m[ERROR]\033[0m $1"; }

# 定义目标路径
TARGET_DIR="/home/root/dockerdata/nginx/html"

info "开始构建项目..."

# 执行构建
if pnpm run build; then
  success "项目构建成功"
else
  error "项目构建失败"
  exit 1
fi

# 检查 dist 目录是否存在
if [ ! -d "dist" ]; then
  error "构建产物 dist 目录不存在，请检查构建过程"
  exit 1
fi

info "清理目标路径中的旧文件..."
# 确保目标目录存在
if [ ! -d "$TARGET_DIR" ]; then
  error "目标目录 $TARGET_DIR 不存在"
  exit 1
fi

# 删除旧的 dist 目录（如果存在）
if [ -d "$TARGET_DIR/dist" ]; then
  if sudo rm -rf "$TARGET_DIR/dist"; then
    success "已删除旧的 dist 目录"
  else
    error "删除旧的 dist 目录失败"
    exit 1
  fi
fi

info "移动新的 dist 目录到目标位置..."
# 移动新的 dist 目录
if sudo mv dist "$TARGET_DIR/"; then
  success "成功将 dist 目录移动到 $TARGET_DIR/"
else
  error "移动 dist 目录失败"
  exit 1
fi

success "构建和部署完成！"