# 对话式问卷管理系统（BED-DialoQ）

基于领域驱动设计的对话式问卷管理系统后端服务，使用Spring Boot开发。

## 项目架构

## 技术栈

- Spring Boot 3.1.0
- Spring Data JPA
- Spring Data Redis
- MySQL 8.x
- OpenAPI (Swagger)

## 功能特性

- 用户管理
- 问卷设计与管理
- 答卷收集与统计
- AI辅助分析

## 系统配置

### 日期时间格式处理

系统对日期时间的处理采用统一格式：`yyyy-MM-dd HH:mm:ss`。

- 通过`JacksonConfig`配置类统一处理JSON序列化和反序列化中的LocalDateTime格式
- 服务实现类中使用相同的DateTimeFormatter处理LocalDateTime转换

### 状态码处理

系统中的状态码处理规则：

- 数据库中状态码使用简短的字符值（如"0", "1", "2"）
- 后端枚举类`QuestionnaireStatus`定义了这些状态码和描述
- 前端枚举类`QuestionnaireStatusEnum`使用更具可读性的名称（如"DRAFT", "PUBLISHED", "OFFLINE"）
- 后端在接收前端请求时会自动将状态名转换为对应的状态码

## 环境要求

- Docker & Docker Compose
- JDK 17
- Maven

## 项目结构

## 快速开始

1. 克隆项目到本地

2. 使用Docker Compose启动项目
```bash
docker-compose up -d
```

3. 服务访问信息
- 应用服务：http://localhost:8080
- Swagger API文档：http://localhost:8080/swagger-ui.html
- MySQL：localhost:3307（外部访问端口）
- Redis：localhost:6379

## 配置信息

### 数据库配置
- 数据库名：bed_dialoq
- 用户名：root
- 密码：password
- 端口：3307（外部访问）

### Redis配置
- 端口：6379
- 无密码认证

## 开发指南

1. 本地开发时，可以直接使用IDE运行`BedDialoQApplication`类
2. 项目使用JPA进行数据库操作，支持自动建表
3. API文档通过Swagger自动生成，便于接口调试和文档维护

## 部署说明

项目采用Docker容器化部署，包含以下服务：
- MySQL数据库服务
- Redis缓存服务
- Spring Boot应用服务

所有服务通过Docker Compose编排，实现一键部署和管理。

## Git开发流程

### 分支管理

项目采用Git Flow工作流：

- `main`: 主分支，用于生产环境
- `develop`: 开发分支，用于开发环境
- `feature/*`: 特性分支，用于开发新功能
- `hotfix/*`: 热修复分支，用于修复生产环境的紧急问题
- `release/*`: 发布分支，用于版本发布

### 提交规范

提交信息格式：
```
<type>(<scope>): <subject>

<body>
```

- type: 提交类型
  - feat: 新功能
  - fix: 修复bug
  - docs: 文档更新
  - style: 代码格式调整
  - refactor: 重构
  - test: 测试相关
  - chore: 构建或辅助工具的变动

- scope: 影响范围（可选）
- subject: 简短描述
- body: 详细说明（可选）

示例：
```
feat(questionnaire): 添加问卷导出功能

- 支持导出为PDF格式
- 添加批量导出功能
```