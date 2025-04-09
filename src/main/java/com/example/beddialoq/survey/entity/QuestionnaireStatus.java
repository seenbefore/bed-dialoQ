package com.example.beddialoq.survey.entity;

/**
 * 问卷状态枚举
 */
public enum QuestionnaireStatus {
    /**
     * 草稿
     */
    DRAFT("0", "草稿"),
    /**
     * 已发布
     */
    PUBLISHED("1", "已发布"),
    /**
     * 已下线
     */
    OFFLINE("2", "已下线");

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
    QuestionnaireStatus(String code, String desc) {
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
    public static QuestionnaireStatus getByCode(String code) {
        for (QuestionnaireStatus status : QuestionnaireStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return DRAFT; // 默认草稿
    }
} 