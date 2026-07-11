package com.course.management.service.impl;

import com.course.management.dto.LoginDTO;
import com.course.management.dto.RegisterDTO;
import com.course.management.entity.Student;
import com.course.management.entity.Teacher;
import com.course.management.entity.User;
import com.course.management.mapper.StudentMapper;
import com.course.management.mapper.TeacherMapper;
import com.course.management.mapper.UserMapper;
import com.course.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public User login(LoginDTO loginDTO) {
        User user = userMapper.findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 修改这里：所有用户都可以用"123456"登录
        String inputPassword = loginDTO.getPassword();

        // 方案1：密码为"123456"时直接通过
        if ("123456".equals(inputPassword)) {
            // 允许登录
        }
        // 方案2：或者使用原密码验证（作为备用方案）
        else if (!user.getPassword().equals(inputPassword)) {
            throw new RuntimeException("密码错误，请使用123456登录");
        }

        if ("INACTIVE".equals(user.getStatus())) {
            throw new RuntimeException("账号已被禁用");
        }

        // 根据用户角色补充信息
        if ("STUDENT".equals(user.getRole())) {
            Student student = studentMapper.findByUserId(user.getId());
            if (student != null) {
                user.setStudentId(student.getStudentId());
                user.setCollege(student.getMajor() + "学院"); // 简化处理
                user.setMajor(student.getMajor());
            }
        } else if ("TEACHER".equals(user.getRole())) {
            Teacher teacher = teacherMapper.findByUserId(user.getId());
            if (teacher != null) {
                user.setStudentId(teacher.getTeacherId());
                user.setCollege(teacher.getDepartment());
                user.setMajor(teacher.getTitle());
            }
        } else if ("ADMIN".equals(user.getRole())) {
            // 管理员用户的额外信息
            user.setStudentId("ADMIN" + user.getId());
            user.setCollege("系统管理");
            user.setMajor("管理员");
        }

        return user;
    }

    @Override
    @Transactional
    public void register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(registerDTO.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword()); // 实际应该加密
        user.setEmail(registerDTO.getEmail());
        user.setRealName(registerDTO.getRealName());
        user.setRole("STUDENT");
        user.setStatus("ACTIVE");

        userMapper.insert(user);
    }

    @Override
    public User getUserInfo(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            return null;
        }

        // 根据用户角色补充信息
        if ("STUDENT".equals(user.getRole())) {
            Student student = studentMapper.findByUserId(user.getId());
            if (student != null) {
                user.setStudentId(student.getStudentId());
                user.setCollege(student.getMajor() + "学院");
                user.setMajor(student.getMajor());
            }
        } else if ("TEACHER".equals(user.getRole())) {
            Teacher teacher = teacherMapper.findByUserId(user.getId());
            if (teacher != null) {
                user.setStudentId(teacher.getTeacherId());
                user.setCollege(teacher.getDepartment());
                user.setMajor(teacher.getTitle());
            }
        } else if ("ADMIN".equals(user.getRole())) {
            user.setStudentId("ADMIN" + user.getId());
            user.setCollege("系统管理");
            user.setMajor("管理员");
        }

        return user;
    }

    @Override
    public List<Map<String, Object>> getUsers(String keyword, String role, String status) {
        return userMapper.findUsers(keyword, role, status);
    }

    @Override
    public void updateUser(User user) {
        userMapper.update(user);
    }

    @Override
    public void deleteUser(Long id) {
        // 这里应该检查是否有依赖数据
        User user = userMapper.findById(id);
        if (user != null) {
            // 实际应该使用逻辑删除
            throw new RuntimeException("用户删除需要处理关联数据");
        }
    }

    @Override
    public void changeStatus(Long id, String status) {
        User user = userMapper.findById(id);
        if (user != null) {
            user.setStatus(status);
            userMapper.update(user);
        }
    }

    // 新增方法：重置用户密码为123456
    @Override
    @Transactional
    public void resetPassword(Long userId, String newPassword) {
        User user = userMapper.findById(userId);
        if (user != null) {
            user.setPassword(newPassword);
            userMapper.update(user);
        }
    }

    // 新增方法：批量重置密码为123456
    @Override
    @Transactional
    public void batchResetPasswords(List<Long> userIds) {
        for (Long userId : userIds) {
            User user = userMapper.findById(userId);
            if (user != null) {
                user.setPassword("123456");
                userMapper.update(user);
            }
        }
    }
}