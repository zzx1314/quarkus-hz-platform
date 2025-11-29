# HZ Server 项目概述

## 1. 项目基本信息

| 项目属性 | 值 |
|---------|---|
| 项目名称 | hz_server |
| 版本 | 1.0.0 |
| 框架 | Quarkus 3.29.3 |
| Java版本 | 21 |
| 数据库 | PostgreSQL 17 |
| 缓存 | Redis 8 |
| 构建工具 | Maven |

## 2. 技术栈

### 2.1 核心框架
- **Quarkus** - 超音速亚原子Java框架，支持原生编译
- **Hibernate ORM Panache** - 简化的ORM框架
- **SmallRye JWT** - JWT认证和授权
- **WebSocket Next** - WebSocket支持

### 2.2 主要依赖
- `quarkus-rest` - RESTful API支持
- `quarkus-rest-jackson` - JSON序列化
- `quarkus-arc` - CDI依赖注入
- `quarkus-hibernate-orm-panache` - 数据库ORM
- `quarkus-jdbc-postgresql` - PostgreSQL驱动
- `quarkus-redis-client` - Redis客户端
- `quarkus-smallrye-jwt` - JWT认证
- `quarkus-websockets-next` - WebSocket支持
- `lombok` - 代码简化
- `mapstruct` - 对象映射

## 3. 项目架构

### 3.1 包结构
```
org.huazhi
├── config/                    # 全局配置
│   └── FileConfig.java
├── drones/                    # 无人机业务模块（核心业务）
│   ├── command/              # 指令管理
│   ├── common/               # 公共组件
│   ├── config/               # 无人机配置
│   ├── device/               # 设备管理
│   ├── media/                # 媒体管理
│   ├── model/                # 型号管理
│   ├── routelibrary/         # 航线库
│   ├── services/             # 服务管理
│   ├── statistics/           # 统计分析
│   ├── task/                 # 任务管理
│   ├── util/                 # 工具类
│   ├── websocket/            # WebSocket通信
│   └── workflow/             # 工作流
├── exception/                 # 异常处理
│   ├── BusinessException.java
│   ├── BusinessExceptionHandler.java
│   └── GlobalExceptionHandler.java
├── oauth/                     # 认证授权
│   ├── controller/
│   ├── entity/
│   └── service/
├── system/                    # 系统管理模块
│   ├── sysauth/              # 权限管理
│   ├── sysdict/              # 字典管理
│   ├── sysdictitem/          # 字典项管理
│   ├── syslog/               # 日志管理
│   ├── sysmenu/              # 菜单管理
│   ├── sysorg/               # 组织管理
│   ├── sysrole/              # 角色管理
│   └── sysuser/              # 用户管理
└── util/                      # 通用工具类
    ├── AESUtils.java         # AES加密
    ├── CommonConstants.java  # 常量定义
    ├── EntityUtils.java      # 实体工具
    ├── FileUtil.java         # 文件工具
    ├── IdUtil.java           # ID生成
    ├── ImageUtil.java        # 图片处理
    ├── JsonUtil.java         # JSON工具
    ├── MD5Util.java          # MD5加密
    ├── PageRequest.java      # 分页请求
    ├── PageResult.java       # 分页结果
    ├── QueryBuilder.java     # 查询构建器
    ├── R.java                # 统一响应
    ├── RedisUtil.java        # Redis工具
    ├── SecurityUtil.java     # 安全工具
    ├── TreeNode.java         # 树节点
    ├── TreeUtil.java         # 树工具
    └── ZipUtil.java          # 压缩工具
```

### 3.2 模块分层架构
每个业务模块采用标准的分层架构：
```
module/
├── controller/    # 控制器层 - 处理HTTP请求
├── entity/        # 实体层 - 数据模型
│   ├── dto/      # 数据传输对象
│   ├── mapper/   # 对象映射器
│   └── vo/       # 视图对象
├── repository/    # 仓库层 - 数据访问
└── service/       # 服务层 - 业务逻辑
    ├── *Service.java      # 服务接口
    └── *ServiceImp.java   # 服务实现
```

## 4. 核心业务模块

### 4.1 无人机设备管理 (drones/device)
- **DronesDevice** - 无人机设备实体
  - 设备ID、类型、状态
  - IP地址、型号
  - 速度、高度、电量
  - 航向、位置信息
  - 通信时间记录

### 4.2 任务管理 (drones/task)
- **DronesTask** - 任务实体
  - 任务名称、描述
  - 任务状态
  - 关联工作流

### 4.3 航线库 (drones/routelibrary)
- 航线规划和管理

### 4.4 媒体管理 (drones/media)
- 无人机拍摄的图片/视频管理

### 4.5 指令管理 (drones/command)
- 无人机控制指令

### 4.6 工作流 (drones/workflow)
- 任务工作流程管理

## 5. 系统管理模块

### 5.1 用户管理 (system/sysuser)
- **SysUser** - 用户实体
  - 账号、密码、姓名
  - 电话、邮件
  - 组织关联
  - 角色关联（多对多）
  - 登录状态管理

### 5.2 角色管理 (system/sysrole)
- 角色定义和权限分配

### 5.3 菜单管理 (system/sysmenu)
- 系统菜单配置

### 5.4 组织管理 (system/sysorg)
- 组织架构管理

### 5.5 字典管理 (system/sysdict, system/sysdictitem)
- 系统字典和字典项

### 5.6 日志管理 (system/syslog)
- 操作日志记录

## 6. 认证授权

### 6.1 JWT认证
- 使用SmallRye JWT实现
- 公钥/私钥签名验证
- Token刷新机制

### 6.2 API端点
- `POST /api/token` - 获取Token
- `GET /api/token/logout` - 登出
- `POST /api/token/refresh/{refreshToken}` - 刷新Token
- `GET /api/token/check_token` - 验证Token

### 6.3 公开路径（无需认证）
- `/api/token` - 登录
- `/api/refresh/*` - Token刷新
- `/api/token/check_token` - Token验证
- `/api/dronesMedia/stream/*` - 媒体流
- `/api/client/clientAuth/*` - 客户端认证
- `/api/client/register` - 客户端注册
- `/api/notice/*` - WebSocket通知

## 7. 部署架构

### 7.1 Docker Compose服务
```yaml
services:
  - postgres      # PostgreSQL数据库 (端口: 5433)
  - redis         # Redis缓存 (端口: 6378)
  - nginx         # Nginx反向代理 (端口: 443, 4433)
  - hz-serve      # 应用服务 (端口: 8080)
```

### 7.2 配置说明
- 开发环境端口: 8081
- 生产环境端口: 8080
- API根路径: `/api`
- 文件存储路径: `/data`

## 8. 客户端支持

项目包含C语言客户端代码：
- `src/main/client/device_client/` - 设备客户端
- `src/main/client/https/` - HTTPS客户端
- `src/main/client/ws/` - WebSocket客户端
- `src/main/client/cer/` - 证书文件

## 9. 代码生成

项目包含Python代码生成器：
- `src/main/gencode/` - 代码生成工具
- 支持生成: Controller, Service, Entity, DTO, Repository, Mapper等

## 10. 运行指南

### 开发模式
```bash
./mvnw quarkus:dev
```

### 打包
```bash
./mvnw package
```

### Docker部署
```bash
docker-compose up -d
```

## 11. 统一响应格式

```java
{
  "code": 0,      // 0-成功, 1-失败
  "msg": "...",   // 消息
  "data": {...}   // 数据
}
```

---

**项目定位**: 这是一个无人机管理系统的后端服务，提供设备管理、任务调度、航线规划、媒体管理等功能，采用现代化的Quarkus框架构建，支持容器化部署。
