package com.example.beddialoq.common;

/**
 * 通用返回结果类
 * @param <T> 返回的数据类型
 */
public class Result<T> {
    
    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 返回消息
     */
    private String message;
    
    /**
     * 返回数据
     */
    private T data;
    
    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 私有构造方法，通过静态方法创建
     */
    private Result() {
    }

    /**
     * 创建成功结果
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setSuccess(true);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * 创建带数据的成功结果
     * @param data 数据
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = success();
        result.setData(data);
        return result;
    }

    /**
     * 创建带消息的成功结果
     * @param message 消息
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> success(String message) {
        Result<T> result = success();
        result.setMessage(message);
        return result;
    }

    /**
     * 创建带数据和消息的成功结果
     * @param data 数据
     * @param message 消息
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> success(T data, String message) {
        Result<T> result = success();
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    /**
     * 创建失败结果
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> error() {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setSuccess(false);
        result.setMessage("操作失败");
        return result;
    }

    /**
     * 创建带消息的失败结果
     * @param message 消息
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = error();
        result.setMessage(message);
        return result;
    }

    /**
     * 创建带状态码和消息的失败结果
     * @param code 状态码
     * @param message 消息
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = error();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    // Getters and Setters
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
} 