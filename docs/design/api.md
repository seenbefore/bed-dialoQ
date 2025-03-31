# API接口文档

本文档详细描述BED-DialoQ（对话式问卷管理系统）MVP版本的模块划分和API接口规范。

## 字段说明

### 通用字段
- **token**: JWT格式的身份认证令牌，有效期为24小时
- **id**: 资源的唯一标识符，使用UUID格式
- **version**: 资源的版本号，用于并发控制
- **status**: 资源状态码，详见状态码定义章节

### 问卷相关字段
- **mode**: 问卷生成模式
  - 1: 快速模式
  - 2: 标准模式
  - 3: 专业模式
- **subject**: 问卷主题
- **objective**: 问卷目标
- **tone**: 问题语气（正式/友好/专业等）
- **language**: 问卷语言
  - 1: 中文
  - 2: 英文
- **model_type**: AI模型类型
  - 1: GPT-3.5
  - 2: GPT-4
- **opening_line**: 问卷开场白

### 问题类型字段
- **type**: 问题类型
  - 1: 单选题
  - 2: 多选题
  - 3: 开放问答
  - 4: 评分题
  - 5: 矩阵量表
- **is_optional**: 是否为可选问题
- **options**: 选项列表（适用于单选、多选类型）
- **skip_logic**: 跳转逻辑配置

### 答卷相关字段
- **form_result**: 答卷结果对象
  - **form_id**: 关联的问卷ID
  - **form_version**: 问卷版本号
  - **responder_email**: 答卷人邮箱
  - **question_and_answers**: 问答记录数组
    - **question**: 问题对象
    - **answer**: 答案对象
      - **text**: 文本答案
      - **multi_selections**: 多选答案数组

### 分析相关字段
- **quality_score**: 答卷质量评分（0-100）
- **summary**: 答卷内容摘要
- **async**: 是否异步处理分析请求


## 模块划分

### 1. 用户管理模块
- 用户注册和登录
- 用户信息管理
- 身份认证和授权

### 2. 问卷管理模块
- 问卷的创建、编辑、删除
- 问卷发布和状态管理
- 问卷模板管理

### 3. 答卷管理模块
- 问卷填答
- 答卷数据存储
- 答卷结果查询

## API接口规范

### 1. 用户管理接口

#### 1.1 用户注册
- **接口**：POST /api/v1/users/register
- **描述**：新用户注册
- **请求参数**：
  ```json
  {
    "username": "string",
    "password": "string",
    "email": "string"
  }
  ```
- **响应格式**：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "userId": "string",
      "username": "string",
      "email": "string"
    }
  }
  ```

#### 1.2 用户登录
- **接口**：POST /api/v1/users/login
- **描述**：用户登录认证
- **请求参数**：
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **响应格式**：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "token": "string",
      "userId": "string",
      "username": "string"
    }
  }
  ```

### 2. 问卷管理接口

#### 2.1 生成问卷
- **接口**：POST /api/v1/forms/generate
- **描述**：根据目标和主题生成问卷
- **请求头**：
  ```
  Authorization: Bearer {token}
  ```
- **请求参数**：
  ```json
  {
    "mode": "number",
    "form": {
      "subject": "string",
      "objective": "string",
      "tone": "string",
      "language": "number",
      "model_type": "number"
    }
  }
  ```
- **响应格式**：
  ```json
  {
    "form": {
      "id": "string",
      "subject": "string",
      "questions": [
        {
          "type": "number",
          "title": "string",
          "options": ["string"],
          "is_optional": "boolean"
        }
      ],
      "version": "number",
      "status": "number",
      "opening_line": "string"
    }
  }
  ```

#### 2.2 更新问卷
- **接口**：POST /api/v1/forms/update
- **描述**：更新问卷内容
- **请求头**：
  ```
  Authorization: Bearer {token}
  ```
- **请求参数**：
  ```json
  {
    "form": {
      "id": "string",
      "subject": "string",
      "questions": [
        {
          "type": "number",
          "title": "string",
          "options": ["string"],
          "is_optional": "boolean"
        }
      ],
      "version": "number",
      "status": "number",
      "opening_line": "string"
    }
  }
  ```
- **响应格式**：
  ```json
  {
    "form": {
      "id": "string",
      "version": "string",
      "status": "string",
      "subject": "string",
      "last_updated": "string"
    }
  }
  ```

### 3. 答卷管理接口

#### 3.1 开始回答问卷
- **接口**：POST /api/v1/forms/shared/{id}/respond
- **描述**：开始回答问卷，获取第一个问题
- **请求参数**：
  ```json
  {
    "form_result": {
      "form_id": "string",
      "form_version": "number",
      "responder_email": "string",
      "question_and_answers": []
    }
  }
  ```
- **响应格式**：
  ```json
  {
    "form_id": "string",
    "form_version": "string",
    "question": {
      "type": "string",
      "title": "string",
      "options": ["string"],
      "skip_logic": {}
    }
  }
  ```

#### 3.2 提交问题答案
- **接口**：POST /api/v1/forms/shared/{id}/respond
- **描述**：提交当前问题答案并获取下一个问题
- **请求参数**：
  ```json
  {
    "form_result": {
      "id": "string",
      "form_id": "string",
      "form_version": "number",
      "question_and_answers": [
        {
          "question": {
            "type": "number",
            "title": "string",
            "options": ["string"]
          },
          "answer": {
            "text": "string",
            "multi_selections": []
          }
        }
      ]
    }
  }
  ```
- **响应格式**：
  ```json
  {
    "form_result": {
      "id": "string",
      "form_id": "string",
      "form_version": "number",
      "question_and_answers": [],
      "current_question_index": "number"
    }
  }
  ```

#### 3.3 获取问卷分析
- **接口**：GET /api/v1/forms/{id}/insight
- **描述**：获取问卷回答的分析结果
- **请求头**：
  ```
  Authorization: Bearer {token}
  ```
- **响应格式**：
  ```json
  {
    "form": {
      "id": "string",
      "questions": [],
      "version": "number"
    },
    "form_result": {
      "id": "string",
      "form_id": "string",
      "question_and_answers": [],
      "summary": {
        "quality_score": "number",
        "summary": "string",
        "finished": "boolean"
      }
    },
    "async": "boolean"
  }
  ```

## 错误码定义

| 错误码 | 描述 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 版本历史

| 版本 | 日期 | 描述 | 作者 |
|------|------|------|------|
| 1.0 | 2024-01-15 | 初始版本，定义基础API接口 | System |
| 1.1 | 2024-01-20 | 补充用户管理、问卷管理和答卷管理接口的详细说明 | System |
| 1.2 | 2024-01-25 | 完善字段说明章节，补充接口参数和响应格式的详细说明 | System |