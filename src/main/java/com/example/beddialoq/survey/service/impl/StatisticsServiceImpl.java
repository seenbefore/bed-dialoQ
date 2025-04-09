package com.example.beddialoq.survey.service.impl;

import com.example.beddialoq.common.BusinessException;
import com.example.beddialoq.survey.dto.StatisticsDTO;
import com.example.beddialoq.survey.entity.Questionnaire;
import com.example.beddialoq.survey.entity.Response;
import com.example.beddialoq.survey.entity.StatisticsCache;
import com.example.beddialoq.survey.repository.QuestionnaireRepository;
import com.example.beddialoq.survey.repository.ResponseRepository;
import com.example.beddialoq.survey.repository.StatisticsCacheRepository;
import com.example.beddialoq.survey.service.StatisticsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    
    private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);
    
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    
    @Autowired
    private ResponseRepository responseRepository;
    
    @Autowired
    private StatisticsCacheRepository statisticsCacheRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public StatisticsDTO getQuestionnaireStatistics(String questionnaireId) {
        // 查询问卷
        Questionnaire questionnaire = questionnaireRepository.findByIdAndIsDeletedFalse(questionnaireId)
                .orElseThrow(() -> new BusinessException("问卷不存在或已被删除"));
        
        // 查询缓存
        Optional<StatisticsCache> cacheOptional = statisticsCacheRepository
                .findByQuestionnaireIdAndCacheTypeAndCacheKey(questionnaireId, "OVERVIEW", "statistics");
        
        // 如果缓存存在且未过期
        if (cacheOptional.isPresent()) {
            StatisticsCache cache = cacheOptional.get();
            if (cache.getExpireTime() == null || cache.getExpireTime().isAfter(LocalDateTime.now())) {
                try {
                    return objectMapper.readValue(cache.getCacheContent(), StatisticsDTO.class);
                } catch (Exception e) {
                    logger.error("解析缓存数据失败", e);
                }
            }
        }
        
        // 缓存不存在或已过期，重新计算
        StatisticsDTO statistics = calculateStatistics(questionnaire);
        
        // 更新缓存
        try {
            String cacheContent = objectMapper.writeValueAsString(statistics);
            
            StatisticsCache cache = cacheOptional.orElse(new StatisticsCache());
            cache.setQuestionnaireId(questionnaireId);
            cache.setCacheType("OVERVIEW");
            cache.setCacheKey("statistics");
            cache.setCacheContent(cacheContent);
            
            // 设置过期时间为1小时后
            cache.setExpireTime(LocalDateTime.now().plusHours(1));
            cache.setUpdateTime(LocalDateTime.now());
            
            statisticsCacheRepository.save(cache);
        } catch (Exception e) {
            logger.error("更新统计缓存失败", e);
        }
        
        return statistics;
    }
    
    @Override
    @Transactional
    public void clearCache(String questionnaireId) {
        List<StatisticsCache> caches = statisticsCacheRepository.findByQuestionnaireId(questionnaireId);
        if (!caches.isEmpty()) {
            statisticsCacheRepository.deleteAll(caches);
        }
    }
    
    @Override
    public void updateCache(String questionnaireId) {
        // 简单实现：直接清除缓存
        clearCache(questionnaireId);
    }
    
    /**
     * 计算问卷统计数据
     * @param questionnaire 问卷
     * @return 统计数据
     */
    private StatisticsDTO calculateStatistics(Questionnaire questionnaire) {
        StatisticsDTO statistics = new StatisticsDTO();
        statistics.setQuestionnaireId(questionnaire.getId());
        statistics.setQuestionnaireTitle(questionnaire.getTitle());
        
        // 查询答卷列表
        List<Response> responses = responseRepository.findByQuestionnaireIdAndIsDeletedFalse(questionnaire.getId());
        
        // 设置总答卷数
        statistics.setTotalResponses(responses.size());
        
        // 计算平均用时
        if (!responses.isEmpty()) {
            Double averageDuration = responseRepository.calculateAverageDuration(questionnaire.getId());
            if (averageDuration != null) {
                statistics.setAverageDuration(averageDuration.intValue());
            }
        }
        
        // 计算答完率（这里简化处理，实际可能需要根据答卷内容判断是否完成）
        statistics.setCompletionRate(1.0);
        
        // 获取每日答卷数量
        calculateDailyResponses(questionnaire.getId(), statistics);
        
        // 解析问卷内容，计算问题统计
        calculateQuestionStats(questionnaire, responses, statistics);
        
        return statistics;
    }
    
    /**
     * 计算每日答卷数量
     * @param questionnaireId 问卷ID
     * @param statistics 统计数据
     */
    private void calculateDailyResponses(String questionnaireId, StatisticsDTO statistics) {
        // 获取最近30天的数据
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        List<Object[]> dailyData = responseRepository.countDailyResponses(questionnaireId, startDate);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<StatisticsDTO.DailyResponseDTO> dailyResponses = new ArrayList<>();
        
        for (Object[] data : dailyData) {
            StatisticsDTO.DailyResponseDTO daily = new StatisticsDTO.DailyResponseDTO();
            daily.setDate(data[0].toString());
            daily.setCount(((Number) data[1]).intValue());
            dailyResponses.add(daily);
        }
        
        statistics.setDailyResponses(dailyResponses);
    }
    
    /**
     * 计算问题统计
     * @param questionnaire 问卷
     * @param responses 答卷列表
     * @param statistics 统计数据
     */
    private void calculateQuestionStats(Questionnaire questionnaire, List<Response> responses, StatisticsDTO statistics) {
        try {
            // 解析问卷内容
            JsonNode questionnaireContent = objectMapper.readTree(questionnaire.getContent());
            
            // 检查是否有questions节点
            if (!questionnaireContent.has("questions")) {
                return;
            }
            
            JsonNode questions = questionnaireContent.get("questions");
            if (!questions.isArray() || questions.isEmpty()) {
                return;
            }
            
            List<StatisticsDTO.QuestionStatDTO> questionStats = new ArrayList<>();
            
            // 遍历每个问题
            for (JsonNode question : questions) {
                StatisticsDTO.QuestionStatDTO questionStat = new StatisticsDTO.QuestionStatDTO();
                questionStat.setQuestionId(question.get("id").asText());
                questionStat.setQuestionTitle(question.get("title").asText());
                questionStat.setQuestionType(question.get("type").asText());
                
                // 根据问题类型处理
                String questionType = question.get("type").asText();
                
                if ("radio".equals(questionType) || "checkbox".equals(questionType) || "dropdown".equals(questionType)) {
                    // 选择题
                    processChoiceQuestion(question, responses, questionStat);
                } else if ("text".equals(questionType) || "textarea".equals(questionType)) {
                    // 文本题
                    processTextQuestion(question, responses, questionStat);
                }
                
                questionStats.add(questionStat);
            }
            
            statistics.setQuestionStats(questionStats);
            
        } catch (Exception e) {
            logger.error("解析问卷内容失败", e);
        }
    }
    
    /**
     * 处理选择题统计
     * @param question 问题
     * @param responses
     * @param questionStat
     */
    private void processChoiceQuestion(JsonNode question, List<Response> responses, StatisticsDTO.QuestionStatDTO questionStat) {
        // 获取选项列表
        JsonNode options = question.get("options");
        if (options == null || !options.isArray()) {
            return;
        }
        
        // 初始化选项计数
        Map<String, Integer> optionCounts = new HashMap<>();
        for (JsonNode option : options) {
            optionCounts.put(option.get("id").asText(), 0);
        }
        
        // 统计每个选项的选择次数
        String questionId = question.get("id").asText();
        for (Response response : responses) {
            try {
                JsonNode responseContent = objectMapper.readTree(response.getContent());
                JsonNode answers = responseContent.get("answers");
                
                if (answers != null && answers.has(questionId)) {
                    JsonNode answer = answers.get(questionId);
                    
                    if (answer.isArray()) {
                        // 多选题
                        for (JsonNode value : answer) {
                            String optionId = value.asText();
                            optionCounts.put(optionId, optionCounts.getOrDefault(optionId, 0) + 1);
                        }
                    } else {
                        // 单选题
                        String optionId = answer.asText();
                        optionCounts.put(optionId, optionCounts.getOrDefault(optionId, 0) + 1);
                    }
                }
            } catch (Exception e) {
                logger.error("解析答卷内容失败", e);
            }
        }
        
        // 创建选项统计
        List<StatisticsDTO.OptionStatDTO> optionStats = new ArrayList<>();
        int totalCount = responses.size();
        
        for (JsonNode option : options) {
            String optionId = option.get("id").asText();
            String optionText = option.get("text").asText();
            int count = optionCounts.getOrDefault(optionId, 0);
            
            StatisticsDTO.OptionStatDTO optionStat = new StatisticsDTO.OptionStatDTO();
            optionStat.setOptionId(optionId);
            optionStat.setOptionText(optionText);
            optionStat.setCount(count);
            
            // 计算百分比
            double percentage = totalCount > 0 ? (double) count / totalCount : 0;
            optionStat.setPercentage(percentage);
            
            optionStats.add(optionStat);
        }
        
        questionStat.setOptionStats(optionStats);
    }
    
    /**
     * 处理文本题统计
     * @param question 问题
     * @param responses 
     * @param questionStat
     */
    private void processTextQuestion(JsonNode question, List<Response> responses, StatisticsDTO.QuestionStatDTO questionStat) {
        List<String> textResponses = new ArrayList<>();
        String questionId = question.get("id").asText();
        
        for (Response response : responses) {
            try {
                JsonNode responseContent = objectMapper.readTree(response.getContent());
                JsonNode answers = responseContent.get("answers");
                
                if (answers != null && answers.has(questionId)) {
                    JsonNode answer = answers.get(questionId);
                    if (answer != null && !answer.isNull()) {
                        textResponses.add(answer.asText());
                    }
                }
            } catch (Exception e) {
                logger.error("解析答卷内容失败", e);
            }
        }
        
        questionStat.setTextResponses(textResponses);
    }
} 