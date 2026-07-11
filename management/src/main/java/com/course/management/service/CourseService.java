package com.course.management.service;

import com.course.management.entity.Course;
import com.course.management.entity.CourseSelection;
import java.util.List;

public interface CourseService {
    List<Course> getAvailableCourses();
    List<Course> getTeacherCourses(Long teacherId);
    List<Course> searchCourses(String keyword, String status);
    Course getCourseDetail(Long courseId);
    void addCourse(Course course);
    void updateCourse(Course course);
    void deleteCourse(Long courseId);

    // 选课相关
    void enrollCourse(Long studentId, Long courseId);
    void withdrawCourse(Long studentId, Long courseId);
    List<CourseSelection> getEnrollmentList(Long courseId);
    List<CourseSelection> getStudentSelections(Long studentId);  // 新增
    void updateGrade(Long studentId, Long courseId, Double score);
}