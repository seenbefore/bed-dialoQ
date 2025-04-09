package com.example.beddialoq.survey.service;

import com.example.beddialoq.survey.dto.StatisticsDTO;

/**
 * 统计服务接口
 */
public interface StatisticsService {
    
    /**
     * 获取问卷统计数据
     * @param questionnaireId 问卷ID
     * @return 统计数据
     */
    StatisticsDTO getQuestionnaireStatistics(String questionnaireId);
    
    /**
     * 清除问卷统计缓存
     * @param questionnaireId 问卷ID
     */
    void clearCache(String questionnaireId);
    
    /**
     * 更新问卷统计缓存（新提交答卷后调用）
     * @param questionnaireId 问卷ID
     */
    void updateCache(String questionnaireId);
} 