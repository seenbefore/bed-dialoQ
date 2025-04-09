package com.example.beddialoq.survey.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * AI分析任务实体类
 */
@Entity
@Table(name = "ai_analysis_task")
public class AiAnalysisTask {
    
    /**
     * 任务ID
     */
    @Id
    @Column(name = "id", length = 32)
    private String id;
    
    /**
     * 答卷ID
     */
    @Column(name = "response_id", length = 32, nullable = false)
    private String responseId;
    
    /**
     * 任务状态：PENDING-待处理, PROCESSING-处理中, COMPLETED-已完成, FAILED-失败
     */
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    
    /**
     * 分析结果
     */
    @Column(name = "result", columnDefinition = "text")
    private String result;
    
    /**
     * 错误信息
     */
    @Column(name = "error_message", length = 500)
    private String errorMessage;
    
    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();
    
    /**
     * 处理时间
     */
    @Column(name = "process_time")
    private LocalDateTime processTime;

    // 无参构造函数
    public AiAnalysisTask() {
    }

    // 生成唯一ID的方法
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        createTime = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getProcessTime() {
        return processTime;
    }

    public void setProcessTime(LocalDateTime processTime) {
        this.processTime = processTime;
    }
} 