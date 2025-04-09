package com.example.beddialoq.common;

import java.util.List;

/**
 * 分页结果类
 * @param <T> 数据类型
 */
public class PageResult<T> {
    /**
     * 总记录数
     */
    private long total;
    
    /**
     * 数据列表
     */
    private List<T> data;
    
    /**
     * 当前页码
     */
    private int page;
    
    /**
     * 每页大小
     */
    private int pageSize;
    
    /**
     * 总页数
     */
    private int totalPages;
    
    /**
     * 私有构造方法，通过静态方法创建
     */
    private PageResult() {
    }
    
    /**
     * 创建分页结果
     * @param data 数据列表
     * @param total 总记录数
     * @param page 当前页码
     * @param pageSize 每页大小
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(List<T> data, long total, int page, int pageSize) {
        PageResult<T> result = new PageResult<>();
        result.setData(data);
        result.setTotal(total);
        result.setPage(page);
        result.setPageSize(pageSize);
        
        // 计算总页数
        int totalPages = (int) Math.ceil((double) total / pageSize);
        result.setTotalPages(totalPages);
        
        return result;
    }

    // Getters and Setters
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
} 