package com.example.beddialoq.survey.controller;

import com.example.beddialoq.common.Result;
import com.example.beddialoq.survey.dto.StatisticsDTO;
import com.example.beddialoq.survey.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 统计控制器
 */
@RestController
@RequestMapping("/survey/statistics")
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    /**
     * 获取问卷统计数据
     * @param questionnaireId 问卷ID
     * @return 统计数据
     */
    @GetMapping("/questionnaire")
    public Result<StatisticsDTO> getQuestionnaireStatistics(
            @RequestParam("questionnaireId") String questionnaireId) {
        StatisticsDTO statistics = statisticsService.getQuestionnaireStatistics(questionnaireId);
        return Result.success(statistics);
    }
    
    /**
     * 清除问卷统计缓存
     * @param questionnaireId 问卷ID
     * @return 结果
     */
    @PostMapping("/clearCache")
    public Result<Void> clearCache(@RequestParam("questionnaireId") String questionnaireId) {
        statisticsService.clearCache(questionnaireId);
        return Result.success();
    }
    
    /**
     * 更新问卷统计缓存
     * @param questionnaireId 问卷ID
     * @return 结果
     */
    @PostMapping("/updateCache")
    public Result<Void> updateCache(@RequestParam("questionnaireId") String questionnaireId) {
        statisticsService.updateCache(questionnaireId);
        return Result.success();
    }
} 