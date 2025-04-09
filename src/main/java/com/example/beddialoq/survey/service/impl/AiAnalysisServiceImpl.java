package com.example.beddialoq.survey.service.impl;

import com.example.beddialoq.common.BusinessException;
import com.example.beddialoq.survey.entity.AiAnalysisTask;
import com.example.beddialoq.survey.entity.Response;
import com.example.beddialoq.survey.repository.AiAnalysisTaskRepository;
import com.example.beddialoq.survey.repository.ResponseRepository;
import com.example.beddialoq.survey.service.AiAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * AI分析服务实现类
 */
@Service
public class AiAnalysisServiceImpl implements AiAnalysisService {
    
    private static final Logger logger = LoggerFactory.getLogger(AiAnalysisServiceImpl.class);
    
    @Autowired
    private AiAnalysisTaskRepository aiAnalysisTaskRepository;
    
    @Autowired
    private ResponseRepository responseRepository;
    
    @Override
    @Transactional
    public String createTask(String responseId) {
        // 查询答卷是否存在
        Response response = responseRepository.findByIdAndIsDeletedFalse(responseId)
                .orElseThrow(() -> new BusinessException("答卷不存在或已被删除"));
        
        // 创建任务
        AiAnalysisTask task = new AiAnalysisTask();
        task.setId(UUID.randomUUID().toString().replace("-", ""));
        task.setResponseId(responseId);
        task.setStatus("PENDING"); // 待处理
        task.setCreateTime(LocalDateTime.now());
        
        // 保存任务
        aiAnalysisTaskRepository.save(task);
        
        // 异步处理任务
        processTaskAsync(task.getId());
        
        return task.getId();
    }
    
    @Async
    public void processTaskAsync(String taskId) {
        try {
            // 等待一段时间模拟异步处理
            Thread.sleep(2000);
            processTask(taskId);
        } catch (InterruptedException e) {
            logger.error("AI分析任务处理被中断: {}", taskId, e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("AI分析任务处理异常: {}", taskId, e);
        }
    }
    
    @Override
    @Transactional
    public void processTask(String taskId) {
        // 查询任务
        AiAnalysisTask task = aiAnalysisTaskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException("任务不存在"));
        
        // 更新任务状态
        task.setStatus("PROCESSING");
        aiAnalysisTaskRepository.save(task);
        
        try {
            // 查询答卷
            Response response = responseRepository.findByIdAndIsDeletedFalse(task.getResponseId())
                    .orElseThrow(() -> new BusinessException("答卷不存在或已被删除"));
            
            // 模拟AI分析过程
            String analysisResult = generateAnalysisResult(response);
            
            // 更新任务状态和结果
            task.setStatus("COMPLETED");
            task.setResult(analysisResult);
            task.setProcessTime(LocalDateTime.now());
            aiAnalysisTaskRepository.save(task);
            
        } catch (Exception e) {
            // 更新任务状态为失败
            task.setStatus("FAILED");
            task.setErrorMessage(e.getMessage());
            task.setProcessTime(LocalDateTime.now());
            aiAnalysisTaskRepository.save(task);
            
            logger.error("AI分析任务处理失败: {}", taskId, e);
            throw e;
        }
    }
    
    @Override
    public AiAnalysisTask getTaskResult(String taskId) {
        return aiAnalysisTaskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException("任务不存在"));
    }
    
    @Override
    public String getAnalysisResult(String responseId) {
        // 查询答卷最新的分析任务
        AiAnalysisTask task = aiAnalysisTaskRepository.findLatestByResponseId(responseId)
                .orElseThrow(() -> new BusinessException("未找到分析任务"));
        
        // 如果任务未完成，抛出异常
        if (!"COMPLETED".equals(task.getStatus())) {
            throw new BusinessException("分析任务尚未完成，当前状态: " + task.getStatus());
        }
        
        return task.getResult();
    }
    
    /**
     * 生成分析结果（模拟AI分析）
     * @param response 答卷
     * @return 分析结果
     */
    private String generateAnalysisResult(Response response) {
        // 这里只是一个简单的模拟，实际项目中可能需要调用外部AI服务
        StringBuilder result = new StringBuilder();
        result.append("AI分析报告\n\n");
        result.append("提交人: ").append(response.getSubmitter()).append("\n");
        result.append("提交时间: ").append(response.getSubmitTime()).append("\n");
        result.append("答题用时: ").append(response.getDuration()).append("秒\n\n");
        result.append("分析结论:\n");
        result.append("1. 根据回答内容，该用户表现出对调查主题的关注度较高\n");
        result.append("2. 答题速度适中，回答内容较为详尽\n");
        result.append("3. 建议关注该用户的后续反馈\n");
        
        return result.toString();
    }
} 