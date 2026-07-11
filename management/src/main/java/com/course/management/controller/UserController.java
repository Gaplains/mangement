package com.course.management.controller;

import com.course.management.dto.Result;
import com.course.management.entity.User;
import com.course.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public Result<List<Map<String, Object>>> getUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {

        // 检查权限（这里简化，实际应该使用拦截器）

        List<Map<String, Object>> users = userService.getUsers(keyword, role, status);
        return Result.success(users);
    }

    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        User user = userService.getUserInfo(id);
        return Result.success(user);
    }

    @PostMapping("")
    public Result<Void> addUser(@RequestBody User user) {
        userService.addUser(user);
        return Result.success("添加成功", null);
    }

    @PutMapping("/{id}")
    public Result<Void> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam String status) {
        userService.changeStatus(id, status);
        return Result.success("状态更新成功", null);
    }

    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestParam(defaultValue = "123456") String password) {
        userService.resetPassword(id, password);
        return Result.success("密码重置成功", null);
    }
}