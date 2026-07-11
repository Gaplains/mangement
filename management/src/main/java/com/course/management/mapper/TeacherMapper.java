package com.course.management.mapper;

import com.course.management.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeacherMapper {

    @Select("SELECT t.*, u.real_name, u.email FROM teachers t " +
            "JOIN users u ON t.user_id = u.id " +
            "WHERE t.id = #{id}")
    Teacher findById(Long id);

    @Select("SELECT t.*, u.real_name, u.email FROM teachers t " +
            "JOIN users u ON t.user_id = u.id " +
            "WHERE t.user_id = #{userId}")
    Teacher findByUserId(Long userId);

    @Select("SELECT t.*, u.real_name, u.email FROM teachers t " +
            "JOIN users u ON t.user_id = u.id")
    List<Teacher> findAll();
}