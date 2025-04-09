package com.example.beddialoq.survey.service;

import com.example.beddialoq.common.PageResult;
import com.example.beddialoq.survey.entity.Response;

import java.util.List;

/**
 * 答卷服务接口
 */
public interface ResponseService {
    
    /**
     * 根据ID查询答卷详情
     * @param id 答卷ID
     * @return 答卷详情
     */
    Response getById(String id);
    
    /**
     * 分页查询答卷列表
     * @param questionnaireId 问卷ID，可为空
     * @param submitter 提交人，可为空
     * @param submitTimeStart 提交开始时间，可为空
     * @param submitTimeEnd 提交结束时间，可为空
     * @param page 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageResult<Response> list(String questionnaireId, String submitter,
                             String submitTimeStart, String submitTimeEnd,
                             Integer page, Integer pageSize);
    
    /**
     * 保存答卷
     * @param response 答卷数据
     * @return 保存后的答卷ID
     */
    String save(Response response);
    
    /**
     * 根据问卷ID查询答卷列表
     * @param questionnaireId 问卷ID
     * @return 答卷列表
     */
    List<Response> listByQuestionnaireId(String questionnaireId);
    
    /**
     * 导出答卷数据
     * @param questionnaireId 问卷ID
     * @return 导出文件的字节数组
     */
    byte[] exportResponses(String questionnaireId);
} 