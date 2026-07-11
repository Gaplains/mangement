package com.course.management.controller;

import com.course.management.dto.Result;
import com.course.management.entity.CourseSelection;
import com.course.management.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/selections")
public class SelectionController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/enroll")
    public Result<Void> enrollCourse(@RequestBody Map<String, Object> params, HttpSession session) {
        try {
            Long studentId = Long.valueOf(params.get("studentId").toString());
            Long courseId = Long.valueOf(params.get("courseId").toString());

            courseService.enrollCourse(studentId, courseId);
            return Result.success("选课成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public Result<Void> withdrawCourse(@RequestBody Map<String, Object> params, HttpSession session) {
        try {
            Long studentId = Long.valueOf(params.get("studentId").toString());
            Long courseId = Long.valueOf(params.get("courseId").toString());

            courseService.withdrawCourse(studentId, courseId);
            return Result.success("退课成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/course/{courseId}")
    public Result<List<CourseSelection>> getEnrollmentList(@PathVariable Long courseId) {
        try {
            List<CourseSelection> list = courseService.getEnrollmentList(courseId);
            return Result.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取选课名单失败");
        }
    }

    @GetMapping("/student")
    public Result<List<CourseSelection>> getStudentSelections(@RequestParam Long studentId) {
        try {
            List<CourseSelection> selections = courseService.getStudentSelections(studentId);
            return Result.success(selections);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取学生选课失败");
        }
    }

    @PutMapping("/grade")
    public Result<Void> updateGrade(@RequestBody Map<String, Object> params) {
        try {
            Long studentId = Long.valueOf(params.get("studentId").toString());
            Long courseId = Long.valueOf(params.get("courseId").toString());
            Double score = Double.valueOf(params.get("score").toString());

            courseService.updateGrade(studentId, courseId, score);
            return Result.success("成绩更新成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}