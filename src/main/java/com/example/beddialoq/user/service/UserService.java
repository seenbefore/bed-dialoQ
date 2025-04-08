package com.example.beddialoq.user.service;

import com.example.beddialoq.user.dto.LoginDto;
import com.example.beddialoq.user.dto.LoginResponseDto;
import com.example.beddialoq.user.dto.PasswordUpdateDto;
import com.example.beddialoq.user.dto.RegisterDto;
import com.example.beddialoq.user.dto.UserDto;
import com.example.beddialoq.user.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     * @param registerDto 注册信息
     * @return 用户信息
     */
    UserDto register(RegisterDto registerDto);
    
    /**
     * 用户登录
     * @param loginDto 登录信息
     * @return 登录响应
     */
    LoginResponseDto login(LoginDto loginDto);
    
    /**
     * 用户登出
     * @param token 访问令牌
     */
    void logout(String token);
    
    /**
     * 获取当前用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    UserDto getCurrentUser(Long userId);
    
    /**
     * 修改密码
     * @param userId 用户ID
     * @param passwordUpdateDto 密码更新信息
     */
    void updatePassword(Long userId, PasswordUpdateDto passwordUpdateDto);
    
    /**
     * 更新用户信息
     * @param userDto 用户信息
     * @return 更新后的用户信息
     */
    UserDto updateUser(UserDto userDto);
    
    /**
     * 将实体转换为DTO
     * @param user 用户实体
     * @return 用户DTO
     */
    UserDto convertToDto(User user);
    
    /**
     * 从DTO更新实体
     * @param user 用户实体
     * @param userDto 用户DTO
     */
    void updateFromDto(User user, UserDto userDto);
} 