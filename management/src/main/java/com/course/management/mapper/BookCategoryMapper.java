package com.course.management.mapper;

import com.course.management.entity.BookCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BookCategoryMapper {

    @Select("SELECT * FROM book_categories ORDER BY sort_order ASC, id ASC")
    List<BookCategory> findAll();

    @Select("SELECT * FROM book_categories WHERE id = #{id}")
    BookCategory findById(Long id);

    @Select("SELECT * FROM book_categories WHERE name = #{name}")
    BookCategory findByName(String name);

    @Insert("INSERT INTO book_categories(name, description, sort_order, created_at) " +
            "VALUES(#{name}, #{description}, #{sortOrder}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BookCategory category);

    @Update("UPDATE book_categories " +
            "SET name = #{name}, description = #{description}, sort_order = #{sortOrder} " +
            "WHERE id = #{id}")
    int update(BookCategory category);

    @Delete("DELETE FROM book_categories WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT COUNT(*) FROM book_categories")
    long countAll();
}
