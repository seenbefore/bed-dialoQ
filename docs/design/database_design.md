# 问卷管理系统数据库设计文档

## 1. 系统概述

本系统是一个具有AI能力的问卷管理系统，支持问卷的创建、编辑、发布、填写和数据分析功能。系统主要包含两个核心模块：问卷管理模块和问卷分析模块。

### 1.1 核心功能

- **问卷管理**：问卷的创建、编辑、发布、预览和填写
- **问卷分析**：问卷数据的统计、可视化和AI分析

### 1.2 技术特点

- 支持多种题型（单选题、多选题、填空题）
- 实时预览问卷效果
- 问卷状态管理（草稿、已发布、已下线）
- 数据可视化展示
- AI分析能力（用户画像、反馈要点、情感分析）

## 2. 数据库表设计

### 2.1 问卷表 (questionnaire)

| 字段名 | 类型 | 长度 | 允许空 | 默认值 | 说明 |
|--------|------|------|--------|--------|------|
| id | varchar | 36 | 否 | - | 主键，问卷ID |
| title | varchar | 100 | 否 | - | 问卷标题 |
| description | varchar | 500 | 是 | null | 问卷描述 |
| content | text | - | 是 | null | 问卷内容，JSON格式 |
| status | char | 1 | 否 | '0' | 发布状态：0草稿，1已发布，2已下线 |
| is_anonymous | tinyint | 1 | 否 | 0 | 是否匿名：0否，1是 |
| publish_time | datetime | - | 是 | null | 发布时间 |
| end_time | datetime | - | 是 | null | 截止时间 |
| create_time | datetime | - | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime | - | 否 | CURRENT_TIMESTAMP | 更新时间 |
| create_by | varchar | 36 | 否 | - | 创建人ID |
| update_by | varchar | 36 | 否 | - | 更新人ID |
| is_deleted | tinyint | 1 | 否 | 0 | 是否删除：0否，1是 |

**索引设计**：
- 主键索引：id
- 普通索引：status, create_time, create_by
- 复合索引：(status, create_time)

### 2.2 答卷表 (response)

| 字段名 | 类型 | 长度 | 允许空 | 默认值 | 说明 |
|--------|------|------|--------|--------|------|
| id | varchar | 36 | 否 | - | 主键，答卷ID |
| questionnaire_id | varchar | 36 | 否 | - | 问卷ID，外键 |
| content | text | - | 否 | - | 答卷内容，JSON格式 |
| submit_time | datetime | - | 否 | CURRENT_TIMESTAMP | 提交时间 |
| submitter | varchar | 50 | 是 | null | 提交人姓名 |
| submitter_id | varchar | 36 | 是 | null | 提交人ID |
| duration | int | 11 | 否 | 0 | 答题时长(秒) |
| ai_summary | text | - | 是 | null | AI分析总结，Markdown格式 |
| ip_address | varchar | 50 | 是 | null | 提交IP地址 |
| user_agent | varchar | 500 | 是 | null | 用户代理信息 |
| is_deleted | tinyint | 1 | 否 | 0 | 是否删除：0否，1是 |

**索引设计**：
- 主键索引：id
- 外键索引：questionnaire_id
- 普通索引：submit_time, submitter_id
- 复合索引：(questionnaire_id, submit_time)

### 2.3 问题表 (question)

| 字段名 | 类型 | 长度 | 允许空 | 默认值 | 说明 |
|--------|------|------|--------|--------|------|
| id | varchar | 36 | 否 | - | 主键，问题ID |
| questionnaire_id | varchar | 36 | 否 | - | 问卷ID，外键 |
| title | varchar | 200 | 否 | - | 问题标题 |
| type | varchar | 20 | 否 | - | 问题类型：singleChoice, multipleChoice, text |
| required | tinyint | 1 | 否 | 0 | 是否必填：0否，1是 |
| sort_order | int | 11 | 否 | 0 | 排序顺序 |
| create_time | datetime | - | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime | - | 否 | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | tinyint | 1 | 否 | 0 | 是否删除：0否，1是 |

**索引设计**：
- 主键索引：id
- 外键索引：questionnaire_id
- 复合索引：(questionnaire_id, sort_order)

### 2.4 选项表 (option)

| 字段名 | 类型 | 长度 | 允许空 | 默认值 | 说明 |
|--------|------|------|--------|--------|------|
| id | varchar | 36 | 否 | - | 主键，选项ID |
| question_id | varchar | 36 | 否 | - | 问题ID，外键 |
| text | varchar | 200 | 否 | - | 选项文本 |
| sort_order | int | 11 | 否 | 0 | 排序顺序 |
| create_time | datetime | - | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime | - | 否 | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | tinyint | 1 | 否 | 0 | 是否删除：0否，1是 |

**索引设计**：
- 主键索引：id
- 外键索引：question_id
- 复合索引：(question_id, sort_order)

### 2.5 答案表 (answer)

| 字段名 | 类型 | 长度 | 允许空 | 默认值 | 说明 |
|--------|------|------|--------|--------|------|
| id | varchar | 36 | 否 | - | 主键，答案ID |
| response_id | varchar | 36 | 否 | - | 答卷ID，外键 |
| question_id | varchar | 36 | 否 | - | 问题ID，外键 |
| option_id | varchar | 36 | 是 | null | 选项ID，外键（文本题无选项） |
| text_content | text | - | 是 | null | 文本答案内容（文本题专用） |
| create_time | datetime | - | 否 | CURRENT_TIMESTAMP | 创建时间 |
| is_deleted | tinyint | 1 | 否 | 0 | 是否删除：0否，1是 |

**索引设计**：
- 主键索引：id
- 外键索引：response_id, question_id, option_id
- 复合索引：(response_id, question_id)

### 2.6 统计缓存表 (statistics_cache)

| 字段名 | 类型 | 长度 | 允许空 | 默认值 | 说明 |
|--------|------|------|--------|--------|------|
| id | varchar | 36 | 否 | - | 主键，缓存ID |
| questionnaire_id | varchar | 36 | 否 | - | 问卷ID，外键 |
| cache_type | varchar | 20 | 否 | - | 缓存类型：overview, question, daily |
| cache_key | varchar | 100 | 否 | - | 缓存键 |
| cache_value | text | - | 否 | - | 缓存值，JSON格式 |
| create_time | datetime | - | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime | - | 否 | CURRENT_TIMESTAMP | 更新时间 |
| expire_time | datetime | - | 是 | null | 过期时间 |

**索引设计**：
- 主键索引：id
- 外键索引：questionnaire_id
- 唯一索引：(questionnaire_id, cache_type, cache_key)
- 普通索引：update_time, expire_time

### 2.7 AI分析任务表 (ai_analysis_task)

| 字段名 | 类型 | 长度 | 允许空 | 默认值 | 说明 |
|--------|------|------|--------|--------|------|
| id | varchar | 36 | 否 | - | 主键，任务ID |
| response_id | varchar | 36 | 否 | - | 答卷ID，外键 |
| status | varchar | 20 | 否 | 'pending' | 任务状态：pending, processing, completed, failed |
| result | text | - | 是 | null | 分析结果，Markdown格式 |
| error_message | varchar | 500 | 是 | null | 错误信息 |
| create_time | datetime | - | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime | - | 否 | CURRENT_TIMESTAMP | 更新时间 |
| process_time | datetime | - | 是 | null | 处理时间 |

**索引设计**：
- 主键索引：id
- 外键索引：response_id
- 普通索引：status, create_time
- 复合索引：(response_id, status)

### 2.8 用户表 (user)

| 字段名 | 类型 | 长度 | 允许空 | 默认值 | 说明 |
|--------|------|------|--------|--------|------|
| id | varchar | 36 | 否 | - | 主键，用户ID |
| username | varchar | 50 | 否 | - | 用户名 |
| password | varchar | 100 | 否 | - | 密码（加密存储） |
| real_name | varchar | 50 | 是 | null | 真实姓名 |
| email | varchar | 100 | 是 | null | 电子邮箱 |
| phone | varchar | 20 | 是 | null | 手机号码 |
| avatar | varchar | 200 | 是 | null | 头像URL |
| status | char | 1 | 否 | '1' | 状态：0禁用，1启用 |
| last_login_time | datetime | - | 是 | null | 最后登录时间 |
| last_login_ip | varchar | 50 | 是 | null | 最后登录IP |
| create_time | datetime | - | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime | - | 否 | CURRENT_TIMESTAMP | 更新时间 |
| is_deleted | tinyint | 1 | 否 | 0 | 是否删除：0否，1是 |

**索引设计**：
- 主键索引：id
- 唯一索引：username, email
- 普通索引：status, create_time
- 复合索引：(status, create_time)

## 3. 表关系说明

### 3.1 主要关系

1. **问卷与问题**：一对多关系
   - 一个问卷可以包含多个问题
   - 每个问题必须属于一个问卷

2. **问题与选项**：一对多关系
   - 一个问题可以包含多个选项（单选题和多选题）
   - 每个选项必须属于一个问题
   - 文本题没有选项

3. **问卷与答卷**：一对多关系
   - 一个问卷可以有多份答卷
   - 每份答卷必须属于一个问卷

4. **答卷与答案**：一对多关系
   - 一份答卷包含多个答案
   - 每个答案必须属于一份答卷

5. **问题与答案**：一对多关系
   - 一个问题可以有多份答案
   - 每个答案必须对应一个问题

6. **选项与答案**：一对多关系
   - 一个选项可以被多个答案选择
   - 每个答案可以选择一个选项（单选题）或多个选项（多选题）
   - 文本题的答案没有关联的选项

7. **用户与问卷**：一对多关系
   - 一个用户可以创建多个问卷
   - 每个问卷必须有一个创建者

9. **用户与答卷**：一对多关系
   - 一个用户可以提交多份答卷
   - 每份答卷可以有一个提交者（非匿名问卷）

### 3.2 实体关系图

```
+----------------+       +----------------+       +----------------+
|  问卷表        |       |  问题表        |       |  选项表        |
| questionnaire  |<----->| question       |<----->| option         |
+----------------+       +----------------+       +----------------+
       |                        |                        |
       |                        |                        |
       v                        v                        v
+----------------+       +----------------+       +----------------+
|  答卷表        |       |  答案表        |       |  AI分析任务表  |
| response       |<----->| answer         |<----->| ai_analysis_task|
+----------------+       +----------------+       +----------------+
       |                        |
       |                        |
       v                        v
+----------------+       +----------------+
|  统计缓存表    |       |  用户表        |
| statistics_cache|       | user          |
+----------------+       +----------------+
```

## 4. 数据存储策略

### 4.1 问卷内容存储

问卷内容采用JSON格式存储在`questionnaire`表的`content`字段中，包含问题、选项等信息。这种设计有以下优点：

1. **灵活性**：可以轻松添加新的问题类型和属性
2. **简化查询**：前端可以直接使用JSON数据渲染问卷
3. **版本兼容**：向后兼容，支持旧版本的数据格式

### 4.2 答卷内容存储

答卷内容也采用JSON格式存储在`response`表的`content`字段中，同时为了支持高效的数据分析，我们将答案拆分为单独的记录存储在`answer`表中。这种设计有以下优点：

1. **完整记录**：保留原始答卷数据，便于回查
2. **高效分析**：通过`answer`表可以快速统计和分析答案数据
3. **数据一致性**：通过外键约束确保数据完整性

### 4.3 统计缓存策略

为了提高统计分析性能，我们设计了`statistics_cache`表来缓存统计结果：

1. **缓存类型**：
   - `overview`：问卷总体统计
   - `question`：问题统计
   - `daily`：每日答卷数量统计

2. **缓存更新策略**：
   - 定时更新：每天凌晨更新所有缓存
   - 触发更新：当有新答卷提交时，更新相关缓存
   - 过期机制：设置缓存过期时间，确保数据及时更新

### 4.4 AI分析策略

AI分析任务通过`ai_analysis_task`表进行管理：

1. **任务状态流转**：
   - `pending`：待处理
   - `processing`：处理中
   - `completed`：已完成
   - `failed`：失败

2. **异步处理**：
   - 当新答卷提交时，创建AI分析任务
   - 后台任务处理系统定期检查待处理任务
   - 处理完成后更新任务状态和结果

## 5. 性能优化建议

### 5.1 索引优化

1. **合理使用索引**：
   - 为常用查询条件创建索引
   - 避免过多索引，影响写入性能
   - 定期分析索引使用情况，优化索引结构

2. **分区策略**：
   - 对大型表（如`response`、`answer`）按时间分区
   - 定期归档历史数据，减少活跃数据量

### 5.2 查询优化

1. **统计查询优化**：
   - 使用统计缓存表减少实时计算
   - 对复杂统计查询使用物化视图
   - 针对大数据量统计使用预计算策略

2. **分页查询优化**：
   - 使用游标分页代替偏移分页
   - 限制单次查询数据量
   - 对热门查询结果进行缓存

### 5.3 数据清理策略

1. **定期清理**：
   - 定期清理已删除的数据
   - 归档长期未访问的历史数据
   - 清理过期的统计缓存

2. **数据备份**：
   - 定期备份重要数据
   - 实现数据恢复机制
   - 保留关键操作日志

## 6. 安全设计

### 6.1 数据安全

1. **敏感信息保护**：
   - 对用户个人信息进行加密存储
   - 匿名问卷不记录提交人信息
   - 实现数据脱敏机制

### 6.2 操作安全

1. **操作日志**：
   - 记录关键操作日志
   - 保留数据修改历史
   - 实现操作审计功能

2. **防攻击措施**：
   - 防止SQL注入
   - 防止XSS攻击
   - 实现请求频率限制

## 7. 扩展性考虑

### 7.1 功能扩展

1. **问题类型扩展**：
   - 设计支持新增问题类型
   - 保持向后兼容性
   - 提供问题类型插件机制

2. **AI能力扩展**：
   - 支持多种AI模型接入
   - 提供AI分析模板配置
   - 支持自定义AI分析规则

### 7.2 性能扩展

1. **水平扩展**：
   - 支持数据库读写分离
   - 实现数据分片策略
   - 支持多实例部署

2. **垂直扩展**：
   - 优化表结构设计
   - 实现数据压缩存储
   - 支持冷热数据分离

## 8. 实施建议

### 8.1 开发阶段

1. **数据库初始化**：
   - 创建数据库和表结构
   - 设置初始索引
   - 配置数据库参数

2. **数据迁移**：
   - 设计数据迁移方案
   - 实现数据转换脚本
   - 验证数据一致性

### 8.2 测试阶段

1. **性能测试**：
   - 模拟高并发场景
   - 测试大数据量处理能力
   - 评估系统响应时间

2. **功能测试**：
   - 验证所有功能点
   - 测试异常处理机制
   - 确保数据一致性

### 8.3 部署阶段

1. **环境准备**：
   - 配置生产环境数据库
   - 设置数据库备份策略
   - 配置监控和告警机制

2. **上线计划**：
   - 制定详细上线步骤
   - 准备回滚方案
   - 安排维护窗口

## 9. 总结

本数据库设计文档详细描述了具有AI能力的问卷管理系统的数据库结构、表关系、存储策略和优化建议。通过合理的数据模型设计和优化策略，可以支持系统的高效运行和未来扩展。

数据库设计遵循了以下原则：
1. **数据完整性**：通过主键、外键和约束确保数据一致性
2. **查询效率**：通过合理的索引设计和缓存策略提高查询性能
3. **扩展性**：设计支持未来功能扩展和性能提升
4. **安全性**：实现数据安全和访问控制机制

通过实施本设计，可以构建一个高效、可靠、安全的问卷管理系统，满足用户的各种需求。 