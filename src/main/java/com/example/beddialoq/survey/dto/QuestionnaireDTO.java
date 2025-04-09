package com.example.beddialoq.survey.dto;

import com.example.beddialoq.common.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 问卷数据传输对象
 */
public class QuestionnaireDTO extends BaseDTO {
    /**
     * ID
     */
    private String id;
    
    /**
     * 问卷标题
     */
    @NotBlank(message = "问卷标题不能为空")
    @Size(max = 100, message = "问卷标题不能超过100个字符")
    private String title;
    
    /**
     * 问卷描述
     */
    @Size(max = 500, message = "问卷描述不能超过500个字符")
    private String description;
    
    /**
     * 问卷内容，JSON格式
     */
    private String content;
    
    /**
     * 发布状态：0草稿，1已发布，2已下线
     */
    @NotBlank(message = "问卷状态不能为空")
    private String status;
    
    /**
     * 是否匿名
     */
    private Boolean isAnonymous;
    
    /**
     * 截止时间
     */
    private LocalDateTime endTime;

    /**
     * 答卷数量（非数据库字段）
     */
    private Integer responseCount;
    
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getResponseCount() {
        return responseCount;
    }

    public void setResponseCount(Integer responseCount) {
        this.responseCount = responseCount;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
} 