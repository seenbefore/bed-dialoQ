package com.example.beddialoq.survey.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 统计缓存实体类
 */
@Entity
@Table(name = "statistics_cache")
public class StatisticsCache {
    
    /**
     * ID
     */
    @Id
    @Column(name = "id", length = 32)
    private String id = UUID.randomUUID().toString().replace("-", "");
    
    /**
     * 问卷ID
     */
    @Column(name = "questionnaire_id", length = 32, nullable = false)
    private String questionnaireId;
    
    /**
     * 缓存类型
     */
    @Column(name = "cache_type", length = 50, nullable = false)
    private String cacheType;
    
    /**
     * 缓存键
     */
    @Column(name = "cache_key", length = 100, nullable = false)
    private String cacheKey;
    
    /**
     * 缓存内容（JSON格式）
     */
    @Column(name = "cache_content", columnDefinition = "text", nullable = false)
    private String cacheContent;
    
    /**
     * 过期时间
     */
    @Column(name = "expire_time")
    private LocalDateTime expireTime;
    
    /**
     * 更新时间
     */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime = LocalDateTime.now();

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

    public String getCacheType() {
        return cacheType;
    }

    public void setCacheType(String cacheType) {
        this.cacheType = cacheType;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheContent() {
        return cacheContent;
    }

    public void setCacheContent(String cacheContent) {
        this.cacheContent = cacheContent;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
} 