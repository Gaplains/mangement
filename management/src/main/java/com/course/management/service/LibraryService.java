package com.course.management.service;

import com.course.management.dto.library.DashboardStatsDTO;
import com.course.management.entity.Book;
import com.course.management.entity.BookCategory;
import com.course.management.entity.BorrowRecord;

import java.util.List;

public interface LibraryService {
    List<Book> searchBooks(String keyword, Long categoryId, String status);

    Book getBook(Long id);

    void addBook(Book book);

    void updateBook(Book book);

    void deleteBook(Long id);

    List<BookCategory> listCategories();

    BookCategory getCategory(Long id);

    void addCategory(BookCategory category);

    void updateCategory(BookCategory category);

    void deleteCategory(Long id);

    void applyBorrow(Long userId, Long bookId, String remark);

    void approveBorrow(Long recordId, String remark);

    void rejectBorrow(Long recordId, String remark);

    void returnBook(Long recordId, String remark);

    List<BorrowRecord> records(Long userId, String status);

    DashboardStatsDTO dashboard();
}
