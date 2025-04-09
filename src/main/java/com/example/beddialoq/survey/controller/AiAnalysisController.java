package com.example.beddialoq.survey.controller;

import com.example.beddialoq.common.Result;
import com.example.beddialoq.survey.entity.AiAnalysisTask;
import com.example.beddialoq.survey.service.AiAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * AI分析控制器
 */
@RestController
@RequestMapping("/survey/ai")
public class AiAnalysisController {
    
    @Autowired
    private AiAnalysisService aiAnalysisService;
    
    /**
     * 创建AI分析任务
     * @param responseId 答卷ID
     * @return 任务ID
     */
    @PostMapping("/createTask")
    public Result<String> createTask(@RequestParam("responseId") String responseId) {
        String taskId = aiAnalysisService.createTask(responseId);
        return Result.success(taskId);
    }
    
    /**
     * 获取AI分析任务结果
     * @param taskId 任务ID
     * @return 任务结果
     */
    @GetMapping("/getResult")
    public Result<Map<String, Object>> getResult(@RequestParam("taskId") String taskId) {
        AiAnalysisTask task = aiAnalysisService.getTaskResult(taskId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("taskId", task.getId());
        result.put("status", task.getStatus());
        result.put("result", task.getResult());
        result.put("errorMessage", task.getErrorMessage());
        result.put("createTime", task.getCreateTime());
        result.put("processTime", task.getProcessTime());
        
        return Result.success(result);
    }
    
    /**
     * 根据答卷ID获取分析结果
     * @param responseId 答卷ID
     * @return 分析结果
     */
    @GetMapping("/getAnalysisResult")
    public Result<String> getAnalysisResult(@RequestParam("responseId") String responseId) {
        String result = aiAnalysisService.getAnalysisResult(responseId);
        return Result.success(result);
    }
} 