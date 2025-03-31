# 对话式调查应用程序系统设计

## 1. 核心数据结构设计

### 1.1 问卷（Form）JSON结构
```json
{
  "id": "UUID",
  "version": "整数/字符串（版本控制）",
  "subject": "问卷主题",
  "objective": "调查目标",
  "status": "状态（如2=已发布）",
  "require_responder_login": "是否需登录",
  "opening_line": "欢迎语",
  "start_button_text": "开始按钮文本",
  "tone": "语气风格（如Professional yet engaging）",
  "customization": {
    "logo_url": "Logo地址",
    "background_color": "背景色",
    "question_color": "问题颜色",
    "button_background_color": "按钮背景色"
  },
  "model_type": "模型类型（如3=GPT-4）",
  "language": "语言（如10=中文）",
  "questions": [
    {
      "type": "问题类型（如3=多选题）",
      "title": "问题标题",
      "options": ["选项1", "选项2"],
      "is_optional": "是否可选",
      "skip_logic": {
        "rules": [
          { "condition": "条件", "target_question_index": "跳转问题索引" }
        ]
      }
    }
  ]
}
```

### 1.2 答卷（FormResult）JSON结构
```json
{
  "id": "UUID",
  "form_id": "关联问卷ID",
  "form_version": "问卷版本",
  "responder_email": "回答者邮箱（可选）",
  "session_id": "UUID（支持断点续答）",
  "device_info": "设备信息（JSON）",
  "question_and_answers": [
    {
      "question": "问题快照",
      "answer": {
        "text": "文本答案",
        "multi_selections": ["多选答案"]
      },
      "original_question_index": "原问题索引"
    }
  ],
  "current_question_index": "当前问题进度",
  "summary": {
    "quality_score": "质量评分",
    "summary": "总结文本",
    "finished": "是否完成"
  }
}
```

## 2. 数据库设计

### 2.1 表结构
| **表名** | **字段** | **说明** |
|----------|----------|----------|
| **users** | `id` (UUID PK), `email`, `password_hash`, `created_at` | 用户表，支持登录和权限管理 |
| **forms** | `id` (UUID PK), `version`, `subject`, `status`, `customization` (JSON), `questions` (JSON), `created_at`, `updated_at` | 问卷主表 |
| **form_permissions** | `user_id` (UUID FK), `form_id` (UUID FK), `role` (ENUM) | 权限关系表，支持多角色（OWNER/EDITOR/VIEWER） |
| **form_results** | `id` (UUID PK), `form_id` (UUID FK), `form_version`, `responder_email`, `session_id` (UUID), `device_info` (JSON), `question_and_answers` (JSON), `summary` (JSON) | 答卷表，支持匿名回答追踪和设备分析 |
| **analytics** | `form_id` (UUID FK), `metric_type` (ENUM), `time_range` (TIMESTAMP), `insights` (JSON), `quality_score`, `raw_data` (JSON), `created_at` | 分析表，支持多维分析 |

### 2.2 索引优化
- **forms表**：`(status, created_at)`复合索引，加速已发布问卷查询
- **form_results表**：`(form_id, created_at)`索引，优化时间范围查询

## 3. 接口设计

### 3.1 认证接口
| **端点** | **方法** | **功能** |
|----------|----------|----------|
| `/auth/register` | POST | 用户注册（邮箱+密码） |
| `/auth/login` | POST | 用户登录（返回JWT令牌） |

### 3.2 问卷管理接口
| **端点** | **方法** | **功能** |
|----------|----------|----------|
| `/generate` | POST | 生成问卷（根据`mode`参数生成主题、问题等） |
| `/update/{formId}` | PUT | 更新问卷元数据（版本控制，权限校验） |
| `/survey/{formId}` | GET | 获取问卷详情（含问题列表和自定义设置） |
| `/survey/{formId}/share` | GET | 生成问卷分享链接（可设置有效期和访问次数限制） |

### 3.3 答卷管理接口
| **端点** | **方法** | **功能** |
|----------|----------|----------|
| `/respond/{formId}` | POST | 提交用户回答（支持断点续答，返回下一问题和预估时间） |
| `/results/{formId}` | GET | 获取所有答卷数据（支持分页和过滤） |
| `/results/{formId}/export` | GET | 导出答卷数据（支持CSV/JSON格式，按时间或用户过滤） |
| `/insight/{formId}` | GET | 获取分析结果（支持动态分析参数和可视化数据） |

### 3.4 WebSocket接口
```plaintext
ws://api.example.com/survey/{formId}/session/{sessionId}
```
- 支持实时推送下一个问题
- 维持心跳包，超时自动保存进度

### 3.5 接口参数说明
- **`mode`参数**：
  - `1`: 生成完整问卷
  - `3`: 仅生成主题
  - `4`: 仅生成欢迎语
- **权限角色**：
  - `1` → `FORM_ROLE_OWNER`
  - `2` → `FORM_ROLE_EDITOR`
  - `3` → `FORM_ROLE_VIEWER`
- **问卷状态**：
  - `1` → 草稿
  - `2` → 已发布（`FORM_STATUS_PUBLISHED`）

## 4. 关键逻辑设计

### 4.1 版本控制
- 每次更新问卷时，`version`递增或切换为语义化版本
- 答卷中记录`form_version`确保数据一致性

### 4.2 动态问题跳转
- 根据`skip_logic`规则和用户答案动态计算`next_question_index`
- 支持条件跳转（如选择"非常不满意