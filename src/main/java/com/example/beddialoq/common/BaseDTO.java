package com.example.beddialoq.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 基础DTO类
 * 所有DTO类都应该继承此类，以获得通用的功能
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseDTO {
    // 可以添加所有DTO共用的字段和方法
} 