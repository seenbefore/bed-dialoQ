# 设计文档

本文档详细描述BED-DialoQ（对话式问卷管理系统）的技术设计方案。

## 系统架构设计

### 1. 整体架构
- 采用DDD（领域驱动设计）架构
- 基于Spring Boot微服务架构
- 前后端分离设计

### 2. 技术栈选型
- 后端：Spring Boot、Spring Cloud、Spring Security
- 数据库：MySQL、Redis
- 消息队列：RabbitMQ
- 搜索引擎：Elasticsearch
- 前端：Vue.js、Element UI

### 3. 系统模块
- 用户认证模块
- 问卷管理模块
- 对话引擎模块
- 数据分析模块
- 系统管理模块

## 数据库设计

### 1. 用户相关表
```sql
-- 用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20),
    status TINYINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 用户配置表
CREATE TABLE user_settings (
    user_id BIGINT PRIMARY KEY,
    preferences JSON,
    notification_config JSON
);
```

### 2. 问卷相关表
```sql
-- 问卷表
CREATE TABLE questionnaires (
    id BIGINT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    creator_id BIGINT,
    status TINYINT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 问题表
CREATE TABLE questions (
    id BIGINT PRIMARY KEY,
    questionnaire_id BIGINT,
    content TEXT NOT NULL,
    type VARCHAR(50),
    options JSON,
    sequence INT,
    required BOOLEAN
);
```

## API设计

### 1. RESTful API规范
- 使用标准HTTP方法（GET, POST, PUT, DELETE）
- 返回标准HTTP状态码
- API版本控制：/api/v1/

### 2. 主要API接口

#### 用户管理
```
POST   /api/v1/users/register     # 用户注册
POST   /api/v1/users/login        # 用户登录
GET    /api/v1/users/profile      # 获取用户信息
PUT    /api/v1/users/profile      # 更新用户信息
```

#### 问卷管理
```
POST   /api/v1/questionnaires     # 创建问卷
GET    /api/v1/questionnaires     # 获取问卷列表
GET    /api/v1/questionnaires/{id} # 获取问卷详情
PUT    /api/v1/questionnaires/{id} # 更新问卷
DELETE /api/v1/questionnaires/{id} # 删除问卷
```

## 安全设计

### 1. 认证与授权
- JWT token认证
- 基于角色的访问控制（RBAC）
- OAuth2.0集成

### 2. 数据安全
- 数据传输加密（HTTPS）
- 敏感数据加密存储
- 数据访问审计日志

## 性能优化

### 1. 缓存策略
- Redis缓存热点数据
- 本地缓存使用Caffeine
- 多级缓存架构

### 2. 数据库优化
- 读写分离
- 分库分表设计
- 索引优化

## 部署架构

### 1. 容器化部署
- 使用Docker容器化
- Kubernetes编排管理
- CI/CD流水线

### 2. 监控告警
- 使用Prometheus + Grafana
- ELK日志分析
- 性能监控和告警

## 版本历史

| 版本 | 日期 | 描述 | 作者 |
|------|------|------|------|
| 1.0 | 2024-01-01 | 初始版本 | System |