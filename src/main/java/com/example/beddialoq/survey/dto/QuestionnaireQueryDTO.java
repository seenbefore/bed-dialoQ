package com.example.beddialoq.survey.dto;

import com.example.beddialoq.common.PageRequestDTO;

/**
 * 问卷查询条件
 */
public class QuestionnaireQueryDTO extends PageRequestDTO {
    /**
     * 问卷标题
     */
    private String title;
    
    /**
     * 发布状态
     */
    private String status;
    
    /**
     * 创建开始时间
     */
    private String createTimeStart;
    
    /**
     * 创建结束时间
     */
    private String createTimeEnd;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }
} 