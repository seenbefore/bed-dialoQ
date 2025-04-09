package com.example.beddialoq.survey.service.impl;

import com.example.beddialoq.common.BusinessException;
import com.example.beddialoq.common.PageResult;
import com.example.beddialoq.survey.entity.Response;
import com.example.beddialoq.survey.repository.ResponseRepository;
import com.example.beddialoq.survey.service.ResponseService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 答卷服务实现类
 */
@Service
public class ResponseServiceImpl implements ResponseService {
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Autowired
    private ResponseRepository responseRepository;
    
    @Override
    public Response getById(String id) {
        return responseRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("答卷不存在或已被删除"));
    }
    
    @Override
    public PageResult<Response> list(String questionnaireId, String submitter,
                                    String submitTimeStart, String submitTimeEnd,
                                    Integer page, Integer pageSize) {
        // 处理分页参数
        int pageNum = page != null ? page - 1 : 0;
        int size = pageSize != null ? pageSize : 10;
        Pageable pageable = PageRequest.of(pageNum, size);
        
        // 处理查询参数
        LocalDateTime startTime = null;
        if (submitTimeStart != null && !submitTimeStart.isEmpty()) {
            // 添加时间部分并解析日期
            startTime = LocalDateTime.parse(submitTimeStart + " 00:00:00", DATE_TIME_FORMATTER);
        }
        
        LocalDateTime endTime = null;
        if (submitTimeEnd != null && !submitTimeEnd.isEmpty()) {
            // 添加时间部分并解析日期
            endTime = LocalDateTime.parse(submitTimeEnd + " 23:59:59", DATE_TIME_FORMATTER);
        }
        
        // 查询数据
        Page<Response> responsePage = responseRepository.findByConditions(
                questionnaireId, submitter, startTime, endTime, pageable);
        
        // 封装结果
        return PageResult.of(responsePage.getContent(), responsePage.getTotalElements(),
                page, pageSize);
    }
    
    @Override
    @Transactional
    public String save(Response response) {
        // 设置ID和提交时间
        if (response.getId() == null || response.getId().isEmpty()) {
            response.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        
        if (response.getSubmitTime() == null) {
            response.setSubmitTime(LocalDateTime.now());
        }
        
        // 保存答卷
        responseRepository.save(response);
        
        return response.getId();
    }
    
    @Override
    public List<Response> listByQuestionnaireId(String questionnaireId) {
        return responseRepository.findByQuestionnaireIdAndIsDeletedFalse(questionnaireId);
    }
    
    @Override
    public byte[] exportResponses(String questionnaireId) {
        List<Response> responses = responseRepository.findByQuestionnaireIdAndIsDeletedFalse(questionnaireId);
        
        if (responses.isEmpty()) {
            throw new BusinessException("没有可导出的答卷数据");
        }
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("答卷数据");
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("答卷ID");
            headerRow.createCell(1).setCellValue("提交人");
            headerRow.createCell(2).setCellValue("提交时间");
            headerRow.createCell(3).setCellValue("用时(秒)");
            headerRow.createCell(4).setCellValue("IP地址");
            headerRow.createCell(5).setCellValue("答卷内容");
            
            // 填充数据行
            for (int i = 0; i < responses.size(); i++) {
                Response response = responses.get(i);
                Row row = sheet.createRow(i + 1);
                
                row.createCell(0).setCellValue(response.getId());
                row.createCell(1).setCellValue(response.getSubmitter());
                
                if (response.getSubmitTime() != null) {
                    row.createCell(2).setCellValue(response.getSubmitTime().format(DATE_TIME_FORMATTER));
                } else {
                    row.createCell(2).setCellValue("");
                }
                
                if (response.getDuration() != null) {
                    row.createCell(3).setCellValue(response.getDuration());
                } else {
                    row.createCell(3).setCellValue(0);
                }
                
                row.createCell(4).setCellValue(response.getIpAddress());
                row.createCell(5).setCellValue(response.getContent());
            }
            
            // 自动调整列宽
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 写入到字节数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new BusinessException("导出答卷数据失败: " + e.getMessage());
        }
    }
} 