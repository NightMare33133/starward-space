#!/bin/bash
# ==============================================================================
# 🌌 Starward Space · 本地开发环境极速启动脚本
# ==============================================================================

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_DIR="$PROJECT_ROOT/starward-backend"
FRONTEND_DIR="$PROJECT_ROOT/starward-frontend"

# 自动定位 Java 21 环境 (适配 Mac Homebrew)
if [ -d "/opt/homebrew/opt/openjdk@21" ]; then
  export JAVA_HOME="/opt/homebrew/opt/openjdk@21"
  export PATH="$JAVA_HOME/bin:$PATH"
fi

check_mysql() {
  echo "🔍 [1/3] 检查 MySQL 8.4 状态..."
  if brew services list 2>/dev/null | grep -q "mysql@8.4.*started"; then
    echo "   ✅ MySQL 8.4 正在后台稳定运行"
  else
    echo "   ⚠️ MySQL 8.4 未启动，正在为你自动拉起..."
    brew services start mysql@8.4
  fi
}

start_backend() {
  check_mysql
  echo "🚀 [2/3] 正在启动 Spring Boot 3 后端服务..."
  echo "   📍 接口基址: http://localhost:8080/api"
  echo "   📖 Swagger 文档: http://localhost:8080/api/swagger-ui/index.html"
  cd "$BACKEND_DIR"
  mvn spring-boot:run
}

start_frontend() {
  echo "🎨 [3/3] 正在启动 Vite 8 前端服务..."
  echo "   🌐 浏览器入口: http://localhost:5173"
  cd "$FRONTEND_DIR"
  npm run dev
}

case "$1" in
  backend|be)
    start_backend
    ;;
  frontend|fe)
    start_frontend
    ;;
  all|"")
    check_mysql
    echo "=================================================================="
    echo "🌌 启动 Starward Space 全栈开发环境"
    echo "   * 后端服务: http://localhost:8080/api"
    echo "   * 接口文档: http://localhost:8080/api/swagger-ui/index.html"
    echo "   * 前端空间: http://localhost:5173"
    echo "   * 提示: 终端按下 [Ctrl + C] 即可一次性安全关闭前后端所有服务"
    echo "=================================================================="
    
    # 捕获退出信号，退出时清理所有后台子进程
    trap 'kill $(jobs -p) 2>/dev/null; exit' SIGINT SIGTERM EXIT
    
    (cd "$BACKEND_DIR" && mvn spring-boot:run) &
    (cd "$FRONTEND_DIR" && npm run dev) &
    
    wait
    ;;
  *)
    echo "用法:"
    echo "  ./dev.sh          # 一键同时启动前后端"
    echo "  ./dev.sh backend  # 仅启动后端 (Spring Boot 3)"
    echo "  ./dev.sh frontend # 仅启动前端 (Vue 3 + Vite)"
    exit 1
    ;;
esac
