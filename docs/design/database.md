# 数据库设计文档

## 1. 概述
本文档详细说明问卷系统的数据库设计，采用简洁的三表设计，通过JSON存储灵活的问卷内容和答案数据。设计注重简单性和可扩展性，同时确保数据的完整性和查询效率。

## 2. 表设计

### 2.1 用户表（users）
存储系统用户信息

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_email (email),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

### 2.2 问卷表（questionnaires）
存储问卷信息，问卷内容以JSON格式存储

```sql
CREATE TABLE questionnaires (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '问卷ID',
    creator_id BIGINT NOT NULL COMMENT '创建者ID',
    title VARCHAR(200) NOT NULL COMMENT '问卷标题',
    content JSON NOT NULL COMMENT '问卷内容（JSON格式）',
    is_published BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否发布',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否删除',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (creator_id) REFERENCES users(id),
    INDEX idx_creator (creator_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问卷表';
```

### 2.3 答卷表（responses）
存储用户的答卷信息，答案内容以JSON格式存储

```sql
CREATE TABLE responses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '答卷ID',
    questionnaire_id BIGINT NOT NULL COMMENT '问卷ID',
    respondent_id BIGINT NOT NULL COMMENT '答题人ID',
    content JSON NOT NULL COMMENT '答卷内容（JSON格式）',
    score INT COMMENT '分数',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (questionnaire_id) REFERENCES questionnaires(id),
    FOREIGN KEY (respondent_id) REFERENCES users(id),
    INDEX idx_questionnaire (questionnaire_id),
    INDEX idx_respondent (respondent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='答卷表';
```

## 3. 表关系说明

1. 用户（users）与问卷（questionnaires）：一对多关系，一个用户可以创建多个问卷
2. 问卷（questionnaires）与答卷（responses）：一对多关系，一个问卷可以有多个答卷
3. 用户（users）与答卷（responses）：一对多关系，一个用户可以提交多个答卷

## 4. 索引设计说明

1. 所有表都使用自增的BIGINT类型作为主键
2. 用户表对username和email建立唯一索引，确保唯一性
3. 问卷表对creator_id和status建立索引，便于按创建者和状态查询
4. 问题表和选项表对其父表ID建立索引，优化关联查询
5. 答卷表对questionnaire_id和respondent_id建立索引，便于统计分析
6. 答案表对response_id和question_id建立索引，优化答卷查询和统计

## 5. 数据安全考虑

1. 密码使用加密存储，不保存明文
2. 支持匿名答卷，respondent_id允许为空
3. 记录答卷IP地址，便于后续数据分析和防刷
4. 使用外键约束确保数据完整性
5. 所有表都包含创建时间，部分表包含更新时间，便于数据追踪

## 6. 扩展性考虑

1. 字段长度预留足够空间
2. 使用utf8mb4字符集，支持完整的Unicode字符
3. 状态字段使用TINYINT，预留足够状态码
4. 问题类型可扩展，支持后续添加新的题型

## 版本历史

| 版本 | 日期 | 描述 | 作者 |
|------|------|------|------|
| 1.0 | 2024-01-01 | 初始版本 | System |
| 1.1 | 2024-01-20 | 完善数据库表设计，添加索引设计和安全性考虑 | System |