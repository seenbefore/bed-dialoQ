# 后端问卷模块使用说明

## 1. 模块概述

问卷模块是对话式问卷管理系统的核心功能模块，负责问卷的全生命周期管理，包括创建、编辑、发布、下线、填写和分析等功能。该模块遵循RESTful API设计规范，提供标准的HTTP接口，支持前端调用。

## 2. 核心功能

- **问卷管理**：问卷的创建、编辑、发布、下线和删除
- **答卷管理**：答卷的提交、查询和导出
- **数据统计**：问卷数据的统计分析
- **AI分析**：基于AI的答卷智能分析

## 3. 技术架构

- **开发语言**：Java 17
- **框架**：Spring Boot 3.1.0
- **数据库**：MySQL 8.0
- **ORM**：Spring Data JPA
- **缓存**：Redis
- **API文档**：SpringDoc OpenAPI

## 4. 数据库设计

问卷模块主要涉及以下数据表：

- `questionnaire`：问卷表
- `response`：答卷表
- `ai_analysis_task`：AI分析任务表
- `statistics_cache`：统计缓存表

详细的表结构请参考`数据库设计文档`。

## 5. API接口说明

### 5.1 问卷管理接口

#### 5.1.1 查询问卷列表

- **URL**：`/survey/questionnaire/list`
- **方法**：POST
- **请求参数**：
  ```json
  {
    "title": "问卷标题",
    "status": "发布状态",
    "createTimeStart": "创建开始时间",
    "createTimeEnd": "创建结束时间",
    "page": 1,
    "pageSize": 10
  }
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "success": true,
    "data": {
      "total": 100,
      "data": [
        {
          "id": "问卷ID",
          "title": "问卷标题",
          "description": "问卷描述",
          "status": "发布状态",
          "responseCount": 10,
          "createTime": "创建时间",
          "updateTime": "更新时间"
        }
      ],
      "page": 1,
      "pageSize": 10,
      "totalPages": 10
    }
  }
  ```

#### 5.1.2 获取问卷详情

- **URL**：`/survey/questionnaire/detail`
- **方法**：GET
- **请求参数**：
  ```
  id=问卷ID
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "success": true,
    "data": {
      "id": "问卷ID",
      "title": "问卷标题",
      "description": "问卷描述",
      "content": "问卷内容JSON",
      "status": "发布状态",
      "isAnonymous": false,
      "publishTime": "发布时间",
      "endTime": "截止时间",
      "responseCount": 10,
      "createTime": "创建时间",
      "updateTime": "更新时间"
    }
  }
  ```

#### 5.1.3 保存问卷

- **URL**：`/survey/questionnaire/save`
- **方法**：POST
- **请求参数**：
  ```json
  {
    "id": "问卷ID（更新时提供，新增时不提供）",
    "title": "问卷标题",
    "description": "问卷描述",
    "content": "问卷内容JSON",
    "status": "发布状态",
    "isAnonymous": false,
    "endTime": "截止时间"
  }
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "success": true,
    "data": "保存后的问卷ID"
  }
  ```

#### 5.1.4 删除问卷

- **URL**：`/survey/questionnaire/remove`
- **方法**：POST
- **请求参数**：
  ```
  id=问卷ID
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "success": true
  }
  ```

#### 5.1.5 更新问卷状态

- **URL**：`/survey/questionnaire/updateStatus`
- **方法**：POST
- **请求参数**：
  ```json
  {
    "id": "问卷ID",
    "status": "目标状态"
  }
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "success": true
  }
  ```

### 5.2 答卷管理接口

#### 5.2.1 查询答卷列表

- **URL**：`/survey/response/list`
- **方法**：POST
- **请求参数**：
  ```
  questionnaireId=问卷ID&submitter=提交人&submitTimeStart=提交开始时间&submitTimeEnd=提交结束时间&page=1&pageSize=10
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "success": true,
    "data": {
      "total": 100,
      "data": [
        {
          "id": "答卷ID",
          "questionnaireId": "问卷ID",
          "submitter": "提交人",
          "submitTime": "提交时间",
          "duration": 60,
          "aiSummary": "AI分析摘要"
        }
      ],
      "page": 1,
      "pageSize": 10,
      "totalPages": 10
    }
  }
  ```

#### 5.2.2 获取答卷详情

- **URL**：`/survey/response/detail`
- **方法**：GET
- **请求参数**：
  ```
  id=答卷ID
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "success": true,
    "data": {
      "id": "答卷ID",
      "questionnaireId": "问卷ID",
      "content": "答卷内容JSON",
      "submitter": "提交人",
      "submitterId": "提交人ID",
      "submitTime": "提交时间",
      "duration": 60,
      "aiSummary": "AI分析摘要",
      "ipAddress": "IP地址",
      "userAgent": "用户代理"
    }
  }
  ```

#### 5.2.3 提交答卷

- **URL**：`/survey/response/submit`
- **方法**：POST
- **请求参数**：
  ```json
  {
    "questionnaireId": "问卷ID",
    "content": "答卷内容JSON",
    "submitter": "提交人",
    "submitterId": "提交人ID",
    "duration": 60
  }
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "success": true,
    "data": "保存后的答卷ID"
  }
  ```

#### 5.2.4 导出答卷数据

- **URL**：`/survey/response/export`
- **方法**：GET
- **请求参数**：
  ```
  questionnaireId=问卷ID
  ```
- **响应**：Excel文件下载

### 5.3 统计分析接口

#### 5.3.1 获取问卷统计数据

- **URL**：`/survey/statistics/questionnaire`
- **方法**：GET
- **请求参数**：
  ```
  questionnaireId=问卷ID
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "success": true,
    "data": {
      "questionnaireId": "问卷ID",
      "questionnaireTitle": "问卷标题",
      "totalResponses": 100,
      "completionRate": 0.95,
      "averageDuration": 60,
      "questionStats": [
        {
          "questionId": "问题ID",
          "questionTitle": "问题标题",
          "questionType": "问题类型",
          "optionStats": [
            {
              "optionId": "选项ID",
              "optionText": "选项文本",
              "count": 50,
              "percentage": 0.5
            }
          ],
          "textResponses": ["文本回答1", "文本回答2"]
        }
      ],
      "dailyResponses": [
        {
          "date": "2023-04-01",
          "count": 10
        }
      ]
    }
  }
  ```

#### 5.3.2 清除问卷统计缓存

- **URL**：`/survey/statistics/clearCache`
- **方法**：POST
- **请求参数**：
  ```
  questionnaireId=问卷ID
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "success": true
  }
  ```

### 5.4 AI分析接口

#### 5.4.1 创建AI分析任务

- **URL**：`/survey/ai/createTask`
- **方法**：POST
- **请求参数**：
  ```
  responseId=答卷ID
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "success": true,
    "data": "任务ID"
  }
  ```

#### 5.4.2 获取AI分析任务结果

- **URL**：`/survey/ai/getResult`
- **方法**：GET
- **请求参数**：
  ```
  taskId=任务ID
  ```
- **响应示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "success": true,
    "data": {
      "taskId": "任务ID",
      "status": "任务状态",
      "result": "分析结果",
      "errorMessage": "错误信息",
      "createTime": "创建时间",
      "processTime": "处理时间"
    }
  }
  ```

## 6. 业务流程

### 6.1 问卷创建与发布流程

1. 创建问卷：通过`/survey/questionnaire/save`接口保存问卷基本信息和内容
2. 编辑问卷：根据需要修改问卷内容
3. 发布问卷：通过`/survey/questionnaire/updateStatus`接口将问卷状态从草稿更改为已发布

### 6.2 问卷填写流程

1. 获取问卷详情：通过`/survey/questionnaire/detail`接口获取问卷内容
2. 填写问卷：用户在前端完成问卷填写
3. 提交答卷：通过`/survey/response/submit`接口提交答卷数据

### 6.3 问卷数据分析流程

1. 查看统计数据：通过`/survey/statistics/questionnaire`接口获取问卷统计数据
2. 查看答卷列表：通过`/survey/response/list`接口获取答卷列表
3. 查看答卷详情：通过`/survey/response/detail`接口获取答卷详情
4. 创建AI分析任务：通过`/survey/ai/createTask`接口创建AI分析任务
5. 获取AI分析结果：通过`/survey/ai/getResult`接口获取AI分析结果

## 7. 注意事项

1. 已发布的问卷不允许修改和删除，只能下线
2. 问卷状态转换有严格的规则：草稿→已发布→已下线→草稿
3. 答卷数据一旦提交不可修改，请在前端做好数据验证
4. AI分析任务是异步处理的，创建任务后需要轮询获取结果
5. 统计数据会使用缓存提高性能，如需实时数据可以调用清除缓存接口

## 8. 后续优化计划

1. 增加问卷模板功能
2. 增加问卷权限控制
3. 优化AI分析算法
4. 增加数据可视化功能
5. 支持问卷分享和嵌入 