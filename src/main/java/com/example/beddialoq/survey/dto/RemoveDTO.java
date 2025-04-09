package com.example.beddialoq.survey.dto;

import com.example.beddialoq.common.BaseDTO;
import jakarta.validation.constraints.NotBlank;

/**
 * 问卷删除请求DTO
 */
public class RemoveDTO extends BaseDTO {
    
    /**
     * 问卷ID
     */
    @NotBlank(message = "问卷ID不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
} 