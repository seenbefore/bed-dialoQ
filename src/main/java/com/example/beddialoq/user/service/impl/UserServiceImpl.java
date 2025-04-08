package com.example.beddialoq.user.service.impl;

import com.example.beddialoq.common.exception.BusinessException;
import com.example.beddialoq.user.dto.LoginDto;
import com.example.beddialoq.user.dto.LoginResponseDto;
import com.example.beddialoq.user.dto.PasswordUpdateDto;
import com.example.beddialoq.user.dto.RegisterDto;
import com.example.beddialoq.user.dto.UserDto;
import com.example.beddialoq.user.entity.User;
import com.example.beddialoq.user.repository.UserRepository;
import com.example.beddialoq.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto register(RegisterDto registerDto) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new BusinessException(400, "用户名已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setRealName(registerDto.getRealName());
        user.setEmail(registerDto.getEmail());
        user.setPhone(registerDto.getPhone());
        user.setLastLoginTime(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());  // 设置创建时间
        
        // 保存用户
        userRepository.save(user);
        
        return convertToDto(user);
    }

    @Override
    public LoginResponseDto login(LoginDto loginDto) {
        // 根据用户名查找用户
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new BusinessException(400, "用户名或密码错误"));
        
        // 校验密码
        if (!user.getPassword().equals(loginDto.getPassword())) {
            throw new BusinessException(400, "用户名或密码错误");
        }
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);
        
        // 组装返回结果
        LoginResponseDto responseDto = new LoginResponseDto();
        // 为了适配前端，生成一个包含特殊格式的token
        String token = "eyJhbGciOiJIUzUxMiJ9.xxxx" + user.getId() + "xxxx" + System.currentTimeMillis();
        responseDto.setToken(token);
        responseDto.setUser(convertToDto(user));
        
        return responseDto;
    }

    @Override
    public void logout(String token) {
        // 简化版，不需要实际处理token
    }

    @Override
    public UserDto getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
        return convertToDto(user);
    }

    @Override
    public void updatePassword(Long userId, PasswordUpdateDto passwordUpdateDto) {
        // 查找用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
        
        // 更新密码
        user.setPassword(passwordUpdateDto.getNewPassword());
        userRepository.save(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        // 查找用户
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
        
        // 更新用户信息
        updateFromDto(user, userDto);
        userRepository.save(user);
        
        return convertToDto(user);
    }

    @Override
    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setRealName(user.getRealName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setAvatar(user.getAvatar());
        userDto.setLastLoginTime(user.getLastLoginTime());
        return userDto;
    }

    @Override
    public void updateFromDto(User user, UserDto userDto) {
        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getRealName() != null) {
            user.setRealName(userDto.getRealName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getPhone() != null) {
            user.setPhone(userDto.getPhone());
        }
        if (userDto.getAvatar() != null) {
            user.setAvatar(userDto.getAvatar());
        }
    }
} 