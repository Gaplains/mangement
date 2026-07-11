package com.course.management.controller;

import com.course.management.dto.Result;
import com.course.management.entity.Course;
import com.course.management.entity.CourseSelection;
import com.course.management.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/available")
    public Result<List<Course>> getAvailableCourses() {
        List<Course> courses = courseService.getAvailableCourses();
        return Result.success(courses);
    }

    @GetMapping("/teacher/{teacherId}")
    public Result<List<Course>> getTeacherCourses(@PathVariable Long teacherId) {
        List<Course> courses = courseService.getTeacherCourses(teacherId);
        return Result.success(courses);
    }

    @GetMapping("/all")
    public Result<List<Course>> getAllCourses() {
        // 新增：获取所有课程（不区分状态）
        List<Course> courses = courseService.searchCourses(null, null);
        return Result.success(courses);
    }

    @GetMapping("/search")
    public Result<List<Course>> searchCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {

        List<Course> courses = courseService.searchCourses(keyword, status);
        return Result.success(courses);
    }

    @GetMapping("/{id}")
    public Result<Course> getCourseDetail(@PathVariable Long id) {
        Course course = courseService.getCourseDetail(id);
        return Result.success(course);
    }

    @PostMapping("")
    public Result<Void> addCourse(@RequestBody Course course, HttpSession session) {
        // 检查用户是否是教师
        // User user = (User) session.getAttribute("user");
        // if (!"teacher".equals(user.getRole()) && !"admin".equals(user.getRole())) {
        //     return Result.error("没有权限发布课程");
        // }

        // 设置默认状态
        course.setStatus("OPEN");

        // 保存课程
        courseService.addCourse(course);
        return Result.success("课程发布成功", null);
    }

    @PutMapping("/{id}")
    public Result<Void> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        try {
            courseService.updateCourse(course);
            return Result.success("课程更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return Result.success("课程删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}