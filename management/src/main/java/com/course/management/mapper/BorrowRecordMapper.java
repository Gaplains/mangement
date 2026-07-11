package com.course.management.mapper;

import com.course.management.entity.BorrowRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BorrowRecordMapper {

    @Select("<script>" +
            "SELECT r.*, u.username, u.real_name, b.title AS book_title, b.isbn " +
            "FROM borrow_records r " +
            "JOIN users u ON r.user_id = u.id " +
            "JOIN books b ON r.book_id = b.id " +
            "WHERE 1 = 1 " +
            "<if test='userId != null'>AND r.user_id = #{userId} </if>" +
            "<if test='status != null and status != \"\"'>AND r.status = #{status} </if>" +
            "ORDER BY r.id DESC" +
            "</script>")
    List<BorrowRecord> search(@Param("userId") Long userId, @Param("status") String status);

    @Select("SELECT * FROM borrow_records WHERE id = #{id}")
    BorrowRecord findById(Long id);

    @Select("SELECT COUNT(*) FROM borrow_records " +
            "WHERE user_id = #{userId} AND book_id = #{bookId} " +
            "AND status IN ('PENDING', 'BORROWED', 'OVERDUE')")
    int activeCount(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Insert("INSERT INTO borrow_records(user_id, book_id, due_date, status, remark, created_at) " +
            "VALUES(#{userId}, #{bookId}, DATE_ADD(NOW(), INTERVAL 30 DAY), 'PENDING', #{remark}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BorrowRecord record);

    @Update("UPDATE borrow_records " +
            "SET status = 'BORROWED', borrow_date = NOW(), due_date = DATE_ADD(NOW(), INTERVAL 30 DAY), remark = #{remark} " +
            "WHERE id = #{id} AND status = 'PENDING'")
    int approve(@Param("id") Long id, @Param("remark") String remark);

    @Update("UPDATE borrow_records " +
            "SET status = 'REJECTED', remark = #{remark} " +
            "WHERE id = #{id} AND status = 'PENDING'")
    int reject(@Param("id") Long id, @Param("remark") String remark);

    @Update("UPDATE borrow_records " +
            "SET status = 'RETURNED', return_date = NOW(), remark = #{remark} " +
            "WHERE id = #{id} AND status IN ('BORROWED', 'OVERDUE')")
    int returnBook(@Param("id") Long id, @Param("remark") String remark);

    @Select("SELECT COUNT(*) FROM borrow_records")
    long countAll();

    @Select("SELECT COUNT(*) FROM borrow_records WHERE status = #{status}")
    long countByStatus(String status);

    @Select("SELECT COUNT(*) FROM borrow_records WHERE book_id = #{bookId}")
    long countByBookId(Long bookId);
}
