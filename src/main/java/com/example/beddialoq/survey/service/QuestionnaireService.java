package com.example.beddialoq.survey.service;

import com.example.beddialoq.common.PageResult;
import com.example.beddialoq.survey.dto.QuestionnaireDTO;
import com.example.beddialoq.survey.dto.QuestionnaireQueryDTO;
import com.example.beddialoq.survey.dto.QuestionnaireStatusDTO;

/**
 * 问卷服务接口
 */
public interface QuestionnaireService {
    
    /**
     * 分页查询问卷列表
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult<QuestionnaireDTO> list(QuestionnaireQueryDTO queryDTO);
    
    /**
     * 根据ID查询问卷详情
     * @param id 问卷ID
     * @return 问卷详情
     */
    QuestionnaireDTO getById(String id);
    
    /**
     * 保存问卷（新增或更新）
     * @param dto 问卷数据
     * @param userId 用户ID
     * @return 保存后的问卷ID
     */
    String save(QuestionnaireDTO dto, String userId);
    
    /**
     * 删除问卷
     * @param id 问卷ID
     */
    void delete(String id);
    
    /**
     * 更新问卷状态
     * @param statusDTO 状态更新数据
     */
    void updateStatus(QuestionnaireStatusDTO statusDTO);
} 