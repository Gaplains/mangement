package com.course.management.mapper;

import com.course.management.entity.Book;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BookMapper {

    @Select("<script>" +
            "SELECT b.*, c.name AS category_name " +
            "FROM books b " +
            "LEFT JOIN book_categories c ON b.category_id = c.id " +
            "WHERE 1 = 1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (b.title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR b.author LIKE CONCAT('%', #{keyword}, '%') " +
            "OR b.isbn LIKE CONCAT('%', #{keyword}, '%') " +
            "OR b.publisher LIKE CONCAT('%', #{keyword}, '%') " +
            "OR b.description LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='categoryId != null'>AND b.category_id = #{categoryId} </if>" +
            "<if test='status != null and status != \"\"'>AND b.status = #{status} </if>" +
            "ORDER BY b.id DESC" +
            "</script>")
    List<Book> search(@Param("keyword") String keyword,
                      @Param("categoryId") Long categoryId,
                      @Param("status") String status);

    @Select("SELECT b.*, c.name AS category_name " +
            "FROM books b " +
            "LEFT JOIN book_categories c ON b.category_id = c.id " +
            "WHERE b.id = #{id}")
    Book findById(Long id);

    @Select("SELECT b.*, c.name AS category_name " +
            "FROM books b " +
            "LEFT JOIN book_categories c ON b.category_id = c.id " +
            "WHERE b.isbn = #{isbn}")
    Book findByIsbn(String isbn);

    @Insert("INSERT INTO books(isbn, title, author, category_id, publisher, publish_year, description, " +
            "total_stock, available_stock, location, status, created_at, updated_at) " +
            "VALUES(#{isbn}, #{title}, #{author}, #{categoryId}, #{publisher}, #{publishYear}, #{description}, " +
            "#{totalStock}, #{availableStock}, #{location}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Book book);

    @Update("UPDATE books SET " +
            "isbn = #{isbn}, " +
            "title = #{title}, " +
            "author = #{author}, " +
            "category_id = #{categoryId}, " +
            "publisher = #{publisher}, " +
            "publish_year = #{publishYear}, " +
            "description = #{description}, " +
            "total_stock = #{totalStock}, " +
            "available_stock = #{availableStock}, " +
            "location = #{location}, " +
            "status = #{status}, " +
            "updated_at = NOW() " +
            "WHERE id = #{id}")
    int update(Book book);

    @Delete("DELETE FROM books WHERE id = #{id}")
    int delete(Long id);

    @Update("UPDATE books " +
            "SET available_stock = available_stock - 1, " +
            "status = IF(available_stock - 1 <= 0, 'OUT_OF_STOCK', 'ON_SHELF'), " +
            "updated_at = NOW() " +
            "WHERE id = #{id} AND available_stock > 0")
    int decreaseStock(Long id);

    @Update("UPDATE books " +
            "SET available_stock = available_stock + 1, " +
            "status = 'ON_SHELF', " +
            "updated_at = NOW() " +
            "WHERE id = #{id} AND available_stock < total_stock")
    int increaseStock(Long id);

    @Select("SELECT COUNT(*) FROM books")
    long countAll();

    @Select("SELECT COUNT(*) FROM books WHERE category_id = #{categoryId}")
    long countByCategoryId(Long categoryId);

    @Select("SELECT COALESCE(SUM(total_stock), 0) FROM books")
    long sumTotalStock();

    @Select("SELECT COALESCE(SUM(available_stock), 0) FROM books")
    long sumAvailableStock();
}
