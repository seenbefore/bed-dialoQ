package com.example.beddialoq.common;

/**
 * 分页请求DTO基类
 * 用于处理分页参数，兼容前端不同的分页参数命名
 */
public abstract class PageRequestDTO extends BaseDTO {
    /**
     * 当前页码
     */
    private Integer page = 1;
    
    /**
     * 兼容前端传入的pageNum参数
     */
    private Integer pageNum;
    
    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 获取页码
     * 如果pageNum有值且page还是默认值，则返回pageNum
     */
    public Integer getPage() {
        return (page == 1 && pageNum != null) ? pageNum : page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
    
    public Integer getPageNum() {
        return pageNum;
    }
    
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        // 同步设置page属性
        if (pageNum != null) {
            this.page = pageNum;
        }
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
} 