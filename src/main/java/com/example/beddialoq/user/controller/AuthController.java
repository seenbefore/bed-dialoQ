package com.example.beddialoq.user.controller;

import com.example.beddialoq.common.Result;
import com.example.beddialoq.user.dto.LoginDto;
import com.example.beddialoq.user.dto.LoginResponseDto;
import com.example.beddialoq.user.dto.PasswordUpdateDto;
import com.example.beddialoq.user.dto.RegisterDto;
import com.example.beddialoq.user.dto.UserDto;
import com.example.beddialoq.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@Tag(name = "用户认证", description = "用户登录、登出、获取信息等接口")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户注册
     * @param registerDto 注册信息
     * @return 注册结果
     */
    @PostMapping("/api/auth/register")
    @Operation(summary = "用户注册", description = "用户注册接口，返回注册成功的用户信息")
    public Result<UserDto> register(@RequestBody RegisterDto registerDto) {
        UserDto userDto = userService.register(registerDto);
        return Result.success(userDto);
    }
    
    /**
     * 用户登录
     * @param loginDto 登录信息
     * @return 登录结果
     */
    @PostMapping("/usercenter/user/login")
    @Operation(summary = "用户登录", description = "用户登录接口，返回用户信息和token")
    public Result<Map<String, Object>> login(@RequestBody LoginDto loginDto) {
        LoginResponseDto loginResponse = userService.login(loginDto);
        
        // 构建前端期望的返回结构
        Map<String, Object> resultData = new HashMap<>();
        
        // 用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("realName", loginResponse.getUser().getRealName());
        userInfo.put("userId", loginResponse.getUser().getId());
        userInfo.put("username", loginResponse.getUser().getUsername());
        userInfo.put("email", loginResponse.getUser().getEmail());
        userInfo.put("phone", loginResponse.getUser().getPhone());
        userInfo.put("avatar", loginResponse.getUser().getAvatar());
        userInfo.put("lastLoginTime", loginResponse.getUser().getLastLoginTime());
        
        resultData.put("user", userInfo);
        resultData.put("token", loginResponse.getToken());
        resultData.put("applicationTypeList", new ArrayList<>());
        resultData.put("xzzfToken", null);
        // 示例菜单
        resultData.put("applicationMenuList", getDefaultMenus());
        
        return Result.success(resultData);
    }
    
    /**
     * 获取公钥
     * @return 公钥信息
     */
    @GetMapping("/usercenter/user/readRsa")
    @Operation(summary = "获取公钥", description = "获取加密公钥")
    public Result<String> getPublicKey() {
        // 为了简化，直接返回一个示例公钥
        return Result.success("-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmweroebYTrMKehZgk0WP15wANffH5rwnz+J31xho2tsITrYzTDPjtG7uVU1tzeMyGSPUy36IAkvwcGo12i4ODogOtPeKgDiKRHOfSuuTSTXl5d/hKTLbhzmxQaT6VCdWsEHw1Zr73MlJ4L961M9tQPIZbdO/gw02Bsftq+RkEO2Fh2A4DDqmTiG7Vc4MlgFWg0m/2ogiPNaa1MLLeAkIfRWxgrlEDiYqlqb0Rwp1+QvhSI+4c2Z6lMaW5M1Cf5pb6GnUstGbT1jnz2XQHrdQCpXdfGYHAWry5Dyimw1jUu3RyFtL59+ZLfIKoEnDh/2CIGU82tc+r0IZ1P7aZo1zhwIDAQAB-----END PUBLIC KEY-----");
    }
    
    /**
     * 通过token登录
     * @return 登录结果
     */
    @GetMapping("/usercenter/user/login-with-token")
    @Operation(summary = "通过token登录", description = "通过token获取用户信息")
    public Result<Map<String, Object>> loginWithToken() {
        // 构建前端期望的返回结构
        Map<String, Object> resultData = new HashMap<>();
        
        // 用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("realName", "系统用户");
        
        resultData.put("user", userInfo);
        resultData.put("applicationTypeList", new ArrayList<>());
        resultData.put("xzzfToken", null);
        // 示例菜单
        resultData.put("applicationMenuList", getDefaultMenus());
        
        return Result.success(resultData);
    }
    
    /**
     * 用户登出
     * @return 登出结果
     */
    @PostMapping("/api/auth/logout")
    @Operation(summary = "用户登出", description = "用户登出接口")
    public Result<?> logout() {
        // 简化版，不需要实际处理token
        return Result.success();
    }
    
    /**
     * 获取当前用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/api/auth/current")
    @Operation(summary = "获取用户信息", description = "获取用户的详细信息")
    public Result<UserDto> getCurrentUser(@RequestParam Long userId) {
        UserDto userDto = userService.getCurrentUser(userId);
        return Result.success(userDto);
    }
    
    /**
     * 修改密码
     * @param userId 用户ID
     * @param passwordUpdateDto 密码更新信息
     * @return 更新结果
     */
    @PostMapping("/api/auth/password")
    @Operation(summary = "修改密码", description = "修改用户的密码")
    public Result<?> updatePassword(@RequestParam Long userId, @RequestBody PasswordUpdateDto passwordUpdateDto) {
        userService.updatePassword(userId, passwordUpdateDto);
        return Result.success();
    }
    
    /**
     * 更新用户信息
     * @param userDto 用户信息
     * @return 更新后的用户信息
     */
    @PostMapping("/api/auth/update")
    @Operation(summary = "更新用户信息", description = "更新用户的基本信息")
    public Result<UserDto> updateUser(@RequestBody UserDto userDto) {
        // 确保userDto中有userId
        if (userDto.getId() == null) {
            return Result.fail(400, "用户ID不能为空");
        }
        UserDto updatedUser = userService.updateUser(userDto);
        return Result.success(updatedUser);
    }
    
    /**
     * 获取默认菜单
     * @return 默认菜单列表
     */
    private ArrayList<Map<String, Object>> getDefaultMenus() {
        ArrayList<Map<String, Object>> menus = new ArrayList<>();
        
        // 创建工作台菜单
        Map<String, Object> workbenchMenu = new HashMap<>();
        workbenchMenu.put("label", "工作台");
        workbenchMenu.put("icon", "el-icon-monitor");
        workbenchMenu.put("uri", "");
        
        // 工作台子菜单
        ArrayList<Map<String, Object>> workbenchChildren = new ArrayList<>();
        
        Map<String, Object> analysisMenu = new HashMap<>();
        analysisMenu.put("label", "分析页");
        analysisMenu.put("uri", "/workbench/analysis");
        
        Map<String, Object> chatMenu = new HashMap<>();
        chatMenu.put("label", "AI助手");
        chatMenu.put("uri", "/workbench/chat");
        
        workbenchChildren.add(analysisMenu);
        workbenchChildren.add(chatMenu);
        workbenchMenu.put("children", workbenchChildren);
        
        // 创建调研管理菜单
        Map<String, Object> surveyMenu = new HashMap<>();
        surveyMenu.put("label", "调研管理");
        surveyMenu.put("icon", "el-icon-document");
        surveyMenu.put("uri", "/调研管理");
        
        // 调研管理子菜单
        ArrayList<Map<String, Object>> surveyChildren = new ArrayList<>();
        
        Map<String, Object> questionnaireMenu = new HashMap<>();
        questionnaireMenu.put("label", "问卷管理");
        questionnaireMenu.put("uri", "/survey/questionnaire");
        
        Map<String, Object> surveyAnalysisMenu = new HashMap<>();
        surveyAnalysisMenu.put("label", "数据分析");
        surveyAnalysisMenu.put("uri", "/survey/analysis");
        
        surveyChildren.add(questionnaireMenu);
        surveyChildren.add(surveyAnalysisMenu);
        surveyMenu.put("children", surveyChildren);
        
        // 创建系统管理菜单
        Map<String, Object> systemMenu = new HashMap<>();
        systemMenu.put("label", "系统管理");
        systemMenu.put("icon", "el-icon-setting");
        systemMenu.put("uri", "/系统管理");
        
        // 系统管理子菜单
        ArrayList<Map<String, Object>> systemChildren = new ArrayList<>();
        
        Map<String, Object> fileManageMenu = new HashMap<>();
        fileManageMenu.put("label", "文件管理");
        fileManageMenu.put("uri", "/system/file-manage");
        
        systemChildren.add(fileManageMenu);
        systemMenu.put("children", systemChildren);
        
        // 添加所有一级菜单
        menus.add(workbenchMenu);
        menus.add(surveyMenu);
        menus.add(systemMenu);
        
        return menus;
    }
} 