package com.course.management.mapper;

import com.course.management.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    @Select("SELECT s.*, u.real_name, u.email FROM students s " +
            "JOIN users u ON s.user_id = u.id " +
            "WHERE s.id = #{id}")
    Student findById(Long id);

    @Select("SELECT s.*, u.real_name, u.email FROM students s " +
            "JOIN users u ON s.user_id = u.id " +
            "WHERE s.user_id = #{userId}")
    Student findByUserId(Long userId);

    @Select("SELECT s.*, u.real_name, u.email FROM students s " +
            "JOIN users u ON s.user_id = u.id")
    List<Student> findAll();

    @Select("SELECT COUNT(*) FROM students")
    int count();
}