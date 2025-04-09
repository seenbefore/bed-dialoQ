package com.example.beddialoq.survey.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 问卷实体类
 */
@Entity
@Table(name = "questionnaire")
public class Questionnaire {
    
    /**
     * ID
     */
    @Id
    @Column(name = "id", length = 32)
    private String id;
    
    /**
     * 问卷标题
     */
    @Column(name = "title", length = 100, nullable = false)
    private String title;
    
    /**
     * 问卷描述
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 问卷内容（JSON格式）
     */
    @Column(name = "content", columnDefinition = "text")
    private String content;
    
    /**
     * 发布状态：0草稿，1已发布，2已下线
     */
    @Column(name = "status", length = 2, nullable = false)
    private String status = "0";
    
    /**
     * 是否匿名
     */
    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous = false;
    
    /**
     * 截止时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    /**
     * 发布时间
     */
    @Column(name = "publish_time")
    private LocalDateTime publishTime;
    
    /**
     * 是否删除
     */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    
    /**
     * 创建人
     */
    @Column(name = "create_by", length = 32)
    private String createBy;
    
    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();
    
    /**
     * 更新人
     */
    @Column(name = "update_by", length = 32)
    private String updateBy;
    
    /**
     * 更新时间
     */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime = LocalDateTime.now();

    // 无参构造函数
    public Questionnaire() {
    }

    // 生成唯一ID的方法
    @PrePersist
    public void prePersist() {
        if (id == null) {
            // 生成适合数据库列长度的ID（最长32位）
            id = UUID.randomUUID().toString().replace("-", "");
            // 确保ID不超过32个字符（列的长度限制）
            if (id.length() > 32) {
                id = id.substring(0, 32);
            }
        }
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
        if (updateTime == null) {
            updateTime = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        updateTime = LocalDateTime.now();
    }

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

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
} 