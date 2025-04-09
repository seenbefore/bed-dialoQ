package com.example.beddialoq.survey.controller;

import com.example.beddialoq.common.BusinessException;
import com.example.beddialoq.common.PageResult;
import com.example.beddialoq.common.Result;
import com.example.beddialoq.survey.dto.QuestionnaireDTO;
import com.example.beddialoq.survey.dto.QuestionnaireQueryDTO;
import com.example.beddialoq.survey.dto.QuestionnaireStatusDTO;
import com.example.beddialoq.survey.dto.RemoveDTO;
import com.example.beddialoq.survey.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

/**
 * 问卷控制器
 */
@RestController
@RequestMapping("/survey/questionnaire")
public class QuestionnaireController {
    
    @Autowired
    private QuestionnaireService questionnaireService;
    
    /**
     * 分页查询问卷列表
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @PostMapping("/list")
    public Result<PageResult<QuestionnaireDTO>> list(@RequestBody QuestionnaireQueryDTO queryDTO) {
        PageResult<QuestionnaireDTO> pageResult = questionnaireService.list(queryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 根据ID查询问卷详情
     * @param id 问卷ID
     * @return 问卷详情
     */
    @GetMapping("/detail")
    public Result<QuestionnaireDTO> detail(@RequestParam("id") String id) {
        QuestionnaireDTO dto = questionnaireService.getById(id);
        return Result.success(dto);
    }
    
    /**
     * 保存问卷（新增或更新）
     * @param dto 问卷数据
     * @return 保存后的问卷ID
     */
    @PostMapping("/save")
    public Result<String> save(@Valid @RequestBody QuestionnaireDTO dto) {
        // TODO: 后续集成用户管理后，从请求中获取用户ID
        String userId = "admin";
        String id = questionnaireService.save(dto, userId);
        return Result.success(id);
    }
    
    /**
     * 删除问卷
     * @param removeDTO 包含问卷ID的请求参数
     * @return 结果
     */
    @PostMapping("/remove")
    public Result<Void> remove(@Valid @RequestBody RemoveDTO removeDTO) {
        questionnaireService.delete(removeDTO.getId());
        return Result.success();
    }
    
    /**
     * 更新问卷状态
     * @param statusDTO 状态更新数据
     * @return 结果
     */
    @PostMapping("/updateStatus")
    public Result<Void> updateStatus(@Valid @RequestBody QuestionnaireStatusDTO statusDTO) {
        questionnaireService.updateStatus(statusDTO);
        return Result.success();
    }
} 