package com.example.beddialoq.survey.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 问卷状态更新DTO
 */
public class QuestionnaireStatusDTO {
    /**
     * 问卷ID
     */
    @NotBlank(message = "问卷ID不能为空")
    private String id;
    
    /**
     * 问卷状态
     */
    @NotBlank(message = "问卷状态不能为空")
    private String status;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 