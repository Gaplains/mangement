package com.course.management.controller;

import com.course.management.dto.LoginDTO;
import com.course.management.dto.RegisterDTO;
import com.course.management.dto.Result;
import com.course.management.entity.User;
import com.course.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        try {
            User user = userService.login(loginDTO);

            // 将用户信息存入session
            session.setAttribute("user", user);

            // 构建返回数据 - 确保数据结构与前端Vue代码匹配
            // 前端期望的数据结构是 { code: 200, msg: '登录成功', data: { user: {...}, token: '...' } }
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("realName", user.getRealName());
            userData.put("email", user.getEmail());
            userData.put("role", user.getRole().toLowerCase()); // 转为小写，便于前端判断
            userData.put("studentId", user.getStudentId());
            userData.put("college", user.getCollege());
            userData.put("major", user.getMajor());
            userData.put("avatar", user.getAvatar());

            // 对于学生用户，需要确保studentId不为空
            if ("student".equals(user.getRole().toLowerCase()) && user.getStudentId() == null) {
                // 如果没有studentId，使用id作为studentId
                userData.put("studentId", "S" + user.getId());
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("user", userData);
            responseData.put("token", "token-" + user.getId() + "-" + System.currentTimeMillis());

            return Result.success("登录成功", responseData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterDTO registerDTO) {
        try {
            userService.register(registerDTO);
            return Result.success("注册成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return Result.success("退出成功", null);
    }

    @GetMapping("/user-info")
    public Result<Map<String, Object>> getUserInfo(HttpSession session) {
        try {
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                return Result.error(401, "未登录");
            }

            User user = userService.getUserInfo(sessionUser.getId());
            if (user == null) {
                return Result.error(404, "用户不存在");
            }

            // 构建用户信息返回数据
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("realName", user.getRealName());
            userData.put("email", user.getEmail());
            userData.put("role", user.getRole().toLowerCase());
            userData.put("studentId", user.getStudentId());
            userData.put("college", user.getCollege());
            userData.put("major", user.getMajor());
            userData.put("avatar", user.getAvatar());

            return Result.success(userData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取用户信息失败");
        }
    }
}