package com.course.management.service;

import com.course.management.dto.LoginDTO;
import com.course.management.dto.RegisterDTO;
import com.course.management.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User login(LoginDTO loginDTO);
    void register(RegisterDTO registerDTO);
    User getUserInfo(Long userId);
    List<Map<String, Object>> getUsers(String keyword, String role, String status);
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    void changeStatus(Long id, String status);

    // 可选：添加新方法
    void resetPassword(Long userId, String newPassword);
    void batchResetPasswords(List<Long> userIds);
}