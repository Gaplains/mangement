package com.course.management.mapper;

import com.course.management.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    // 修改这个查询方法
    @Select("<script>" +
            "SELECT c.*, u.real_name as teacher_name FROM courses c " +
            "LEFT JOIN teachers t ON c.teacher_id = t.id " +
            "LEFT JOIN users u ON t.user_id = u.id " +
            "WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "   AND (c.course_name LIKE CONCAT('%', #{keyword}, '%') " +
            "        OR c.course_code LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "   AND c.status = #{status}" +
            "</if>" +
            "<if test='status == null or status == \"\"'>" +
            "   AND c.status = 'OPEN'" +  // 默认只显示OPEN状态的课程
            "</if>" +
            "ORDER BY c.id DESC" +
            "</script>")
    List<Course> searchCourses(@Param("keyword") String keyword, @Param("status") String status);

    // 新增：获取所有课程（不带状态过滤）
    @Select("SELECT c.*, u.real_name as teacher_name FROM courses c " +
            "LEFT JOIN teachers t ON c.teacher_id = t.id " +
            "LEFT JOIN users u ON t.user_id = u.id " +
            "ORDER BY c.id DESC")
    List<Course> findAll();

    // 修改：获取教师课程时去掉状态过滤
    @Select("SELECT c.*, u.real_name as teacher_name FROM courses c " +
            "LEFT JOIN teachers t ON c.teacher_id = t.id " +
            "LEFT JOIN users u ON t.user_id = u.id " +
            "WHERE c.teacher_id = #{teacherId} " +
            "ORDER BY c.id DESC")
    List<Course> findByTeacherId(Long teacherId);

    @Select("SELECT c.*, u.real_name as teacher_name FROM courses c " +
            "LEFT JOIN teachers t ON c.teacher_id = t.id " +
            "LEFT JOIN users u ON t.user_id = u.id " +
            "WHERE c.id = #{courseId}")
    Course findById(Long courseId);

    @Insert("INSERT INTO courses(course_code, course_name, credit, description, teacher_id, " +
            "max_students, current_students, status, semester, created_at) " +
            "VALUES(#{courseCode}, #{courseName}, #{credit}, #{description}, #{teacherId}, " +
            "#{maxStudents}, 0, 'OPEN', #{semester}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Course course);

    @Update("UPDATE courses SET " +
            "course_name = #{courseName}, " +
            "credit = #{credit}, " +
            "description = #{description}, " +
            "max_students = #{maxStudents}, " +
            "status = #{status}, " +
            "semester = #{semester}, " +
            "updated_at = NOW() " +
            "WHERE id = #{id}")
    int update(Course course);

    @Delete("DELETE FROM courses WHERE id = #{courseId}")
    int delete(Long courseId);

    @Update("UPDATE courses SET current_students = current_students + 1 WHERE id = #{courseId}")
    int increaseStudentCount(Long courseId);

    @Update("UPDATE courses SET current_students = current_students - 1 WHERE id = #{courseId}")
    int decreaseStudentCount(Long courseId);
}