package com.example.beddialoq.survey.service;

import com.example.beddialoq.survey.entity.AiAnalysisTask;

/**
 * AI分析服务接口
 */
public interface AiAnalysisService {
    
    /**
     * 创建AI分析任务
     * @param responseId 答卷ID
     * @return 任务ID
     */
    String createTask(String responseId);
    
    /**
     * 处理AI分析任务
     * @param taskId 任务ID
     */
    void processTask(String taskId);
    
    /**
     * 获取任务结果
     * @param taskId 任务ID
     * @return 任务信息
     */
    AiAnalysisTask getTaskResult(String taskId);
    
    /**
     * 根据答卷ID获取最新的分析结果
     * @param responseId 答卷ID
     * @return 分析结果
     */
    String getAnalysisResult(String responseId);
} 