package com.example.beddialoq.survey.entity;

/**
 * AI任务状态枚举
 */
public enum AiTaskStatus {
    
    /**
     * 等待处理
     */
    PENDING("PENDING", "等待处理"),
    
    /**
     * 处理中
     */
    PROCESSING("PROCESSING", "处理中"),
    
    /**
     * 处理完成
     */
    COMPLETED("COMPLETED", "处理完成"),
    
    /**
     * 处理失败
     */
    FAILED("FAILED", "处理失败");
    
    /**
     * 状态码
     */
    private final String code;
    
    /**
     * 状态描述
     */
    private final String desc;
    
    /**
     * 构造方法
     * @param code 状态码
     * @param desc 状态描述
     */
    AiTaskStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 获取状态码
     * @return 状态码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 获取状态描述
     * @return 状态描述
     */
    public String getDesc() {
        return desc;
    }
    
    /**
     * 根据状态码获取枚举
     * @param code 状态码
     * @return 枚举
     */
    public static AiTaskStatus getByCode(String code) {
        for (AiTaskStatus status : AiTaskStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return PENDING; // 默认等待处理
    }
} 