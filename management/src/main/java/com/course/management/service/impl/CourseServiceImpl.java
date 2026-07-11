package com.course.management.service.impl;

import com.course.management.entity.Course;
import com.course.management.entity.CourseSelection;
import com.course.management.mapper.CourseMapper;
import com.course.management.mapper.CourseSelectionMapper;
import com.course.management.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    @Override
    public List<Course> getAvailableCourses() {
        // 改为获取所有OPEN状态的课程
        return courseMapper.searchCourses(null, "OPEN");
    }

    @Override
    public List<Course> getTeacherCourses(Long teacherId) {
        return courseMapper.findByTeacherId(teacherId);
    }

    @Override
    public List<Course> searchCourses(String keyword, String status) {
        if (keyword == null && (status == null || status.isEmpty())) {
            // 如果没有搜索条件，返回所有课程
            return courseMapper.findAll();
        }
        return courseMapper.searchCourses(keyword, status);
    }

    @Override
    public Course getCourseDetail(Long courseId) {
        return courseMapper.findById(courseId);
    }

    // 新增：获取所有课程（用于课程管理）
    public List<Course> getAllCourses() {
        return courseMapper.findAll();
    }

    @Override
    public void addCourse(Course course) {
        courseMapper.insert(course);
    }

    @Override
    public void updateCourse(Course course) {
        // 首先检查课程是否存在
        Course existingCourse = courseMapper.findById(course.getId());
        if (existingCourse == null) {
            throw new RuntimeException("课程不存在");
        }

        // 检查是否有学生选课，如果课程已有关联学生，不能减少容量
        if (course.getMaxStudents() != null &&
                course.getMaxStudents() < existingCourse.getCurrentStudents()) {
            throw new RuntimeException("课程容量不能小于当前选课人数");
        }

        // 更新课程
        courseMapper.update(course);
    }

    @Override
    public void deleteCourse(Long courseId) {
        // 检查是否有学生选课
        List<CourseSelection> selections = courseSelectionMapper.findByCourseId(courseId);
        if (!selections.isEmpty()) {
            throw new RuntimeException("课程有学生选课，不能删除");
        }
        // 删除课程
        int result = courseMapper.delete(courseId);
        if (result == 0) {
            throw new RuntimeException("课程删除失败");
        }
    }

    @Override
    @Transactional
    public void enrollCourse(Long studentId, Long courseId) {
        // 检查是否已选
        int count = courseSelectionMapper.checkSelection(studentId, courseId);
        if (count > 0) {
            throw new RuntimeException("已选过该课程");
        }

        // 检查课程容量
        Course course = courseMapper.findById(courseId);
        if (course.getCurrentStudents() >= course.getMaxStudents()) {
            throw new RuntimeException("课程容量已满");
        }

        // 选课
        CourseSelection selection = new CourseSelection();
        selection.setStudentId(studentId);
        selection.setCourseId(courseId);
        courseSelectionMapper.insert(selection);

        // 更新课程人数
        courseMapper.increaseStudentCount(courseId);
    }

    @Override
    @Transactional
    public void withdrawCourse(Long studentId, Long courseId) {
        // 检查是否已选
        int count = courseSelectionMapper.checkSelection(studentId, courseId);
        if (count == 0) {
            throw new RuntimeException("未选该课程");
        }

        // 退课
        courseSelectionMapper.delete(studentId, courseId);

        // 更新课程人数
        courseMapper.decreaseStudentCount(courseId);
    }

    @Override
    public List<CourseSelection> getEnrollmentList(Long courseId) {
        return courseSelectionMapper.findByCourseId(courseId);
    }

    @Override
    public List<CourseSelection> getStudentSelections(Long studentId) {
        return courseSelectionMapper.findByStudentId(studentId);
    }

    @Override
    public void updateGrade(Long studentId, Long courseId, Double score) {
        courseSelectionMapper.updateScore(studentId, courseId, score);
    }
}