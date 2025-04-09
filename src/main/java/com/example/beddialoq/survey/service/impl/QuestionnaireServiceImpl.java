package com.example.beddialoq.survey.service.impl;

import com.example.beddialoq.common.BusinessException;
import com.example.beddialoq.common.PageResult;
import com.example.beddialoq.survey.dto.QuestionnaireDTO;
import com.example.beddialoq.survey.dto.QuestionnaireQueryDTO;
import com.example.beddialoq.survey.dto.QuestionnaireStatusDTO;
import com.example.beddialoq.survey.entity.Questionnaire;
import com.example.beddialoq.survey.entity.QuestionnaireStatus;
import com.example.beddialoq.survey.repository.QuestionnaireRepository;
import com.example.beddialoq.survey.service.QuestionnaireService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 问卷服务实现类
 */
@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    
    @Override
    public PageResult<QuestionnaireDTO> list(QuestionnaireQueryDTO queryDTO) {
        // 处理查询参数
        String title = queryDTO.getTitle();
        String status = queryDTO.getStatus();
        
        LocalDateTime createTimeStart = null;
        if (queryDTO.getCreateTimeStart() != null && !queryDTO.getCreateTimeStart().isEmpty()) {
            createTimeStart = LocalDateTime.parse(queryDTO.getCreateTimeStart() + " 00:00:00", 
                    DATE_TIME_FORMATTER);
        }
        
        LocalDateTime createTimeEnd = null;
        if (queryDTO.getCreateTimeEnd() != null && !queryDTO.getCreateTimeEnd().isEmpty()) {
            createTimeEnd = LocalDateTime.parse(queryDTO.getCreateTimeEnd() + " 23:59:59", 
                    DATE_TIME_FORMATTER);
        }
        
        // 创建分页参数
        int page = queryDTO.getPage() != null ? queryDTO.getPage() - 1 : 0;
        int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        
        // 查询数据
        Page<Questionnaire> questionnaireList = questionnaireRepository.findByConditions(
                title, status, createTimeStart, createTimeEnd, pageable);
        
        // 转换为DTO
        List<QuestionnaireDTO> dtoList = questionnaireList.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        // 查询答卷数量
        dtoList.forEach(dto -> {
            long responseCount = questionnaireRepository.countResponsesByQuestionnaireId(dto.getId());
            dto.setResponseCount((int) responseCount);
        });
        
        // 封装分页结果
        return PageResult.of(dtoList, questionnaireList.getTotalElements(), 
                queryDTO.getPage(), queryDTO.getPageSize());
    }
    
    @Override
    public QuestionnaireDTO getById(String id) {
        Questionnaire questionnaire = questionnaireRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("问卷不存在或已被删除"));
        
        QuestionnaireDTO dto = convertToDTO(questionnaire);
        
        // 查询答卷数量
        long responseCount = questionnaireRepository.countResponsesByQuestionnaireId(id);
        dto.setResponseCount((int) responseCount);
        
        return dto;
    }
    
    @Override
    @Transactional
    public String save(QuestionnaireDTO dto, String userId) {
        Questionnaire questionnaire;
        
        // 处理状态值，前端可能传入的是枚举名称（如"DRAFT"），需要转换为对应的状态码
        if (dto.getStatus() != null) {
            // 如果状态值长度超过2，说明是枚举名称，需要转换
            if (dto.getStatus().length() > 2) {
                switch (dto.getStatus()) {
                    case "DRAFT":
                        dto.setStatus(QuestionnaireStatus.DRAFT.getCode());
                        break;
                    case "PUBLISHED":
                        dto.setStatus(QuestionnaireStatus.PUBLISHED.getCode());
                        break;
                    case "OFFLINE":
                        dto.setStatus(QuestionnaireStatus.OFFLINE.getCode());
                        break;
                    default:
                        // 默认设置为草稿状态
                        dto.setStatus(QuestionnaireStatus.DRAFT.getCode());
                }
            }
        }
        
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            // 更新
            questionnaire = questionnaireRepository.findByIdAndIsDeletedFalse(dto.getId())
                    .orElseThrow(() -> new BusinessException("问卷不存在或已被删除"));
            
            // 已发布的问卷不允许修改
            if (QuestionnaireStatus.PUBLISHED.getCode().equals(questionnaire.getStatus())) {
                throw new BusinessException("已发布的问卷不允许修改");
            }
            
            BeanUtils.copyProperties(dto, questionnaire, "id", "createTime", "createBy", "isDeleted");
            questionnaire.setUpdateBy(userId);
            questionnaire.setUpdateTime(LocalDateTime.now());
        } else {
            // 新增
            questionnaire = new Questionnaire();
            BeanUtils.copyProperties(dto, questionnaire);
            
            // 生成适合数据库列长度的ID（最长32位）
            // 使用UUID并去除连字符，确保长度不超过32
            String newId = UUID.randomUUID().toString().replace("-", "");
            if (newId.length() > 32) {
                newId = newId.substring(0, 32);
            }
            questionnaire.setId(newId);
            
            questionnaire.setCreateBy(userId);
            questionnaire.setUpdateBy(userId);
        }
        
        // 检查状态是否需要更新
        if (QuestionnaireStatus.PUBLISHED.getCode().equals(questionnaire.getStatus()) && 
                questionnaire.getPublishTime() == null) {
            // 新发布问卷
            questionnaire.setPublishTime(LocalDateTime.now());
        }
        
        // 保存
        questionnaireRepository.save(questionnaire);
        
        return questionnaire.getId();
    }
    
    @Override
    @Transactional
    public void delete(String id) {
        Questionnaire questionnaire = questionnaireRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("问卷不存在或已被删除"));
        
        // 已发布的问卷不允许删除
        if (QuestionnaireStatus.PUBLISHED.getCode().equals(questionnaire.getStatus())) {
            throw new BusinessException("已发布的问卷不允许删除");
        }
        
        // 软删除
        questionnaire.setIsDeleted(true);
        questionnaire.setUpdateTime(LocalDateTime.now());
        
        questionnaireRepository.save(questionnaire);
    }
    
    @Override
    @Transactional
    public void updateStatus(QuestionnaireStatusDTO statusDTO) {
        Questionnaire questionnaire = questionnaireRepository.findByIdAndIsDeletedFalse(statusDTO.getId())
                .orElseThrow(() -> new BusinessException("问卷不存在或已被删除"));
        
        String status = statusDTO.getStatus();
        
        // 处理状态值，前端可能传入的是枚举名称（如"DRAFT"），需要转换为对应的状态码
        if (status != null && status.length() > 2) {
            switch (status) {
                case "DRAFT":
                    status = QuestionnaireStatus.DRAFT.getCode();
                    break;
                case "PUBLISHED":
                    status = QuestionnaireStatus.PUBLISHED.getCode();
                    break;
                case "OFFLINE":
                    status = QuestionnaireStatus.OFFLINE.getCode();
                    break;
                default:
                    // 不处理
                    break;
            }
            statusDTO.setStatus(status);
        }
        
        String oldStatus = questionnaire.getStatus();
        String newStatus = statusDTO.getStatus();
        
        // 检查状态转换是否合法
        if (oldStatus.equals(newStatus)) {
            throw new BusinessException("问卷状态已经是" + QuestionnaireStatus.getByCode(newStatus).getDesc());
        }
        
        // 草稿只能发布，已发布只能下线，已下线只能变为草稿
        if (QuestionnaireStatus.DRAFT.getCode().equals(oldStatus) && 
                !QuestionnaireStatus.PUBLISHED.getCode().equals(newStatus)) {
            throw new BusinessException("草稿问卷只能变更为已发布状态");
        }
        
        if (QuestionnaireStatus.PUBLISHED.getCode().equals(oldStatus) && 
                !QuestionnaireStatus.OFFLINE.getCode().equals(newStatus)) {
            throw new BusinessException("已发布问卷只能变更为已下线状态");
        }
        
        if (QuestionnaireStatus.OFFLINE.getCode().equals(oldStatus) && 
                !QuestionnaireStatus.DRAFT.getCode().equals(newStatus)) {
            throw new BusinessException("已下线问卷只能变更为草稿状态");
        }
        
        // 更新状态
        questionnaire.setStatus(newStatus);
        questionnaire.setUpdateTime(LocalDateTime.now());
        
        // 如果是发布操作，记录发布时间
        if (QuestionnaireStatus.PUBLISHED.getCode().equals(newStatus)) {
            questionnaire.setPublishTime(LocalDateTime.now());
        }
        
        questionnaireRepository.save(questionnaire);
    }
    
    /**
     * 将实体转换为DTO
     * @param questionnaire 问卷实体
     * @return 问卷DTO
     */
    private QuestionnaireDTO convertToDTO(Questionnaire questionnaire) {
        QuestionnaireDTO dto = new QuestionnaireDTO();
        BeanUtils.copyProperties(questionnaire, dto);
        return dto;
    }
} 