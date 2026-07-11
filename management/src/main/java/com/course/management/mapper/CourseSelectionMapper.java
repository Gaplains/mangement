package com.course.management.mapper;

import com.course.management.entity.CourseSelection;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseSelectionMapper {

    @Select("SELECT cs.*, u.real_name as student_name, c.course_name " +
            "FROM course_selections cs " +
            "JOIN students s ON cs.student_id = s.id " +
            "JOIN users u ON s.user_id = u.id " +
            "JOIN courses c ON cs.course_id = c.id " +
            "WHERE cs.student_id = #{studentId}")
    List<CourseSelection> findByStudentId(Long studentId);

    @Select("SELECT cs.*, u.real_name as student_name, c.course_name " +
            "FROM course_selections cs " +
            "JOIN students s ON cs.student_id = s.id " +
            "JOIN users u ON s.user_id = u.id " +
            "JOIN courses c ON cs.course_id = c.id " +
            "WHERE cs.course_id = #{courseId}")
    List<CourseSelection> findByCourseId(Long courseId);

    @Select("SELECT COUNT(*) FROM course_selections WHERE student_id = #{studentId} AND course_id = #{courseId}")
    int checkSelection(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Insert("INSERT INTO course_selections(student_id, course_id, status) VALUES(#{studentId}, #{courseId}, 'SELECTED')")
    int insert(CourseSelection selection);

    @Delete("DELETE FROM course_selections WHERE student_id = #{studentId} AND course_id = #{courseId}")
    int delete(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Update("UPDATE course_selections SET final_score = #{score} WHERE student_id = #{studentId} AND course_id = #{courseId}")
    int updateScore(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("score") Double score);
}