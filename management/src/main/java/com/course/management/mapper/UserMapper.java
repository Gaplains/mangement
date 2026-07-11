package com.course.management.mapper;

import com.course.management.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);

    @Insert("INSERT INTO users(username, password, role, email, phone, real_name, status) " +
            "VALUES(#{username}, #{password}, #{role}, #{email}, #{phone}, #{realName}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE users SET email=#{email}, phone=#{phone}, real_name=#{realName}, status=#{status} " +
            "WHERE id=#{id}")
    int update(User user);

    @Update("UPDATE users SET password=#{password} WHERE id=#{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Select("<script>" +
            "SELECT u.*, s.student_id, t.teacher_id FROM users u " +
            "LEFT JOIN students s ON u.id = s.user_id " +
            "LEFT JOIN teachers t ON u.id = t.user_id " +
            "WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "   AND (u.username LIKE CONCAT('%', #{keyword}, '%') OR u.real_name LIKE CONCAT('%', #{keyword}, '%') OR u.email LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if>" +
            "<if test='role != null and role != \"\"'>" +
            "   AND u.role = #{role}" +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "   AND u.status = #{status}" +
            "</if>" +
            "ORDER BY u.id DESC" +
            "</script>")
    List<Map<String, Object>> findUsers(@Param("keyword") String keyword,
                                        @Param("role") String role,
                                        @Param("status") String status);


    @Select("SELECT COUNT(*) FROM users WHERE role = #{role}")
    long countByRole(String role);


    @Update("UPDATE users SET username=#{username}, role=#{role}, email=#{email}, phone=#{phone}, real_name=#{realName}, status=#{status} WHERE id=#{id}")
    int updateFull(User user);

    @Update("UPDATE users SET status='INACTIVE' WHERE id=#{id}")
    int deactivate(Long id);
}
