package com.example.beddialoq.survey.controller;

import com.example.beddialoq.common.PageResult;
import com.example.beddialoq.common.Result;
import com.example.beddialoq.survey.entity.Response;
import com.example.beddialoq.survey.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 答卷控制器
 */
@RestController
@RequestMapping("/survey/response")
public class ResponseController {
    
    @Autowired
    private ResponseService responseService;
    
    /**
     * 分页查询答卷列表
     * @param questionnaireId 问卷ID
     * @param submitter 提交人
     * @param submitTimeStart 提交开始时间
     * @param submitTimeEnd 提交结束时间
     * @param page 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    @PostMapping("/list")
    public Result<PageResult<Response>> list(
            @RequestParam(value = "questionnaireId", required = false) String questionnaireId,
            @RequestParam(value = "submitter", required = false) String submitter,
            @RequestParam(value = "submitTimeStart", required = false) String submitTimeStart,
            @RequestParam(value = "submitTimeEnd", required = false) String submitTimeEnd,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PageResult<Response> pageResult = responseService.list(
                questionnaireId, submitter, submitTimeStart, submitTimeEnd, page, pageSize);
        return Result.success(pageResult);
    }
    
    /**
     * 根据ID查询答卷详情
     * @param id 答卷ID
     * @return 答卷详情
     */
    @GetMapping("/detail")
    public Result<Response> detail(@RequestParam("id") String id) {
        Response response = responseService.getById(id);
        return Result.success(response);
    }
    
    /**
     * 提交答卷
     * @param response 答卷数据
     * @return 保存后的答卷ID
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Response response) {
        String id = responseService.save(response);
        return Result.success(id);
    }
    
    /**
     * 导出答卷数据
     * @param questionnaireId 问卷ID
     * @return 导出文件
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> export(@RequestParam("questionnaireId") String questionnaireId) {
        byte[] data = responseService.exportResponses(questionnaireId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String filename = "responses_" + questionnaireId + ".xlsx";
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        headers.setContentDispositionFormData("attachment", encodedFilename);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }
} 