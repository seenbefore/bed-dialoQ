package com.example.beddialoq.survey.dto;

import java.util.List;

/**
 * 统计数据传输对象
 */
public class StatisticsDTO {
    /**
     * 问卷ID
     */
    private String questionnaireId;
    
    /**
     * 问卷标题
     */
    private String questionnaireTitle;
    
    /**
     * 总答卷数
     */
    private Integer totalResponses;
    
    /**
     * 答完率
     */
    private Double completionRate;
    
    /**
     * 平均花费时间(秒)
     */
    private Integer averageDuration;
    
    /**
     * 问题统计
     */
    private List<QuestionStatDTO> questionStats;
    
    /**
     * 每日答卷数量统计
     */
    private List<DailyResponseDTO> dailyResponses;

    // Getters and Setters
    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getQuestionnaireTitle() {
        return questionnaireTitle;
    }

    public void setQuestionnaireTitle(String questionnaireTitle) {
        this.questionnaireTitle = questionnaireTitle;
    }

    public Integer getTotalResponses() {
        return totalResponses;
    }

    public void setTotalResponses(Integer totalResponses) {
        this.totalResponses = totalResponses;
    }

    public Double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }

    public Integer getAverageDuration() {
        return averageDuration;
    }

    public void setAverageDuration(Integer averageDuration) {
        this.averageDuration = averageDuration;
    }

    public List<QuestionStatDTO> getQuestionStats() {
        return questionStats;
    }

    public void setQuestionStats(List<QuestionStatDTO> questionStats) {
        this.questionStats = questionStats;
    }

    public List<DailyResponseDTO> getDailyResponses() {
        return dailyResponses;
    }

    public void setDailyResponses(List<DailyResponseDTO> dailyResponses) {
        this.dailyResponses = dailyResponses;
    }

    /**
     * 问题统计DTO
     */
    public static class QuestionStatDTO {
        /**
         * 问题ID
         */
        private String questionId;
        
        /**
         * 问题标题
         */
        private String questionTitle;
        
        /**
         * 问题类型
         */
        private String questionType;
        
        /**
         * 选项统计
         */
        private List<OptionStatDTO> optionStats;
        
        /**
         * 文本回答
         */
        private List<String> textResponses;

        // Getters and Setters
        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public String getQuestionTitle() {
            return questionTitle;
        }

        public void setQuestionTitle(String questionTitle) {
            this.questionTitle = questionTitle;
        }

        public String getQuestionType() {
            return questionType;
        }

        public void setQuestionType(String questionType) {
            this.questionType = questionType;
        }

        public List<OptionStatDTO> getOptionStats() {
            return optionStats;
        }

        public void setOptionStats(List<OptionStatDTO> optionStats) {
            this.optionStats = optionStats;
        }

        public List<String> getTextResponses() {
            return textResponses;
        }

        public void setTextResponses(List<String> textResponses) {
            this.textResponses = textResponses;
        }
    }

    /**
     * 选项统计DTO
     */
    public static class OptionStatDTO {
        /**
         * 选项ID
         */
        private String optionId;
        
        /**
         * 选项文本
         */
        private String optionText;
        
        /**
         * 选择次数
         */
        private Integer count;
        
        /**
         * 选择百分比
         */
        private Double percentage;

        // Getters and Setters
        public String getOptionId() {
            return optionId;
        }

        public void setOptionId(String optionId) {
            this.optionId = optionId;
        }

        public String getOptionText() {
            return optionText;
        }

        public void setOptionText(String optionText) {
            this.optionText = optionText;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }
    }

    /**
     * 每日答卷数量DTO
     */
    public static class DailyResponseDTO {
        /**
         * 日期
         */
        private String date;
        
        /**
         * 答卷数量
         */
        private Integer count;

        // Getters and Setters
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }
} 