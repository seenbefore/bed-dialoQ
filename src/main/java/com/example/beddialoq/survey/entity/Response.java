package com.example.beddialoq.survey.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 答卷实体类
 */
@Entity
@Table(name = "response")
public class Response {
    
    /**
     * ID
     */
    @Id
    @Column(name = "id", length = 32)
    private String id;
    
    /**
     * 问卷ID
     */
    @Column(name = "questionnaire_id", length = 32, nullable = false)
    private String questionnaireId;
    
    /**
     * 答卷内容（JSON格式）
     */
    @Column(name = "content", columnDefinition = "text", nullable = false)
    private String content;
    
    /**
     * 提交人
     */
    @Column(name = "submitter", length = 50)
    private String submitter;
    
    /**
     * 提交人ID
     */
    @Column(name = "submitter_id", length = 32)
    private String submitterId;
    
    /**
     * 提交时间
     */
    @Column(name = "submit_time")
    private LocalDateTime submitTime;
    
    /**
     * 用时（秒）
     */
    @Column(name = "duration")
    private Integer duration;
    
    /**
     * IP地址
     */
    @Column(name = "ip_address", length = 50)
    private String ipAddress;
    
    /**
     * 用户代理
     */
    @Column(name = "user_agent", length = 500)
    private String userAgent;
    
    /**
     * AI分析摘要
     */
    @Column(name = "ai_summary", length = 500)
    private String aiSummary;
    
    /**
     * 是否删除
     */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    
    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();
    
    /**
     * 更新时间
     */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime = LocalDateTime.now();

    // 无参构造函数
    public Response() {
    }

    // 生成唯一ID的方法
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        if (submitTime == null) {
            submitTime = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(String submitterId) {
        this.submitterId = submitterId;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAiSummary() {
        return aiSummary;
    }

    public void setAiSummary(String aiSummary) {
        this.aiSummary = aiSummary;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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