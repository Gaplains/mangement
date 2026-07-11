package com.course.management.controller;

import com.course.management.dto.Result;
import com.course.management.dto.library.BorrowApplyDTO;
import com.course.management.dto.library.BorrowHandleDTO;
import com.course.management.dto.library.DashboardStatsDTO;
import com.course.management.entity.Book;
import com.course.management.entity.BookCategory;
import com.course.management.entity.BorrowRecord;
import com.course.management.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @GetMapping("/books")
    public Result<List<Book>> searchBooks(@RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) Long categoryId,
                                          @RequestParam(required = false) String status) {
        return Result.success(libraryService.searchBooks(keyword, categoryId, status));
    }

    @GetMapping("/books/{id}")
    public Result<Book> getBook(@PathVariable Long id) {
        return Result.success(libraryService.getBook(id));
    }

    @PostMapping("/books")
    public Result<Void> addBook(@RequestBody Book book) {
        libraryService.addBook(book);
        return Result.success("图书添加成功", null);
    }

    @PutMapping("/books/{id}")
    public Result<Void> updateBook(@PathVariable Long id, @RequestBody Book book) {
        book.setId(id);
        libraryService.updateBook(book);
        return Result.success("图书更新成功", null);
    }

    @DeleteMapping("/books/{id}")
    public Result<Void> deleteBook(@PathVariable Long id) {
        libraryService.deleteBook(id);
        return Result.success("图书删除成功", null);
    }

    @GetMapping("/categories")
    public Result<List<BookCategory>> listCategories() {
        return Result.success(libraryService.listCategories());
    }

    @GetMapping("/categories/{id}")
    public Result<BookCategory> getCategory(@PathVariable Long id) {
        return Result.success(libraryService.getCategory(id));
    }

    @PostMapping("/categories")
    public Result<Void> addCategory(@RequestBody BookCategory category) {
        libraryService.addCategory(category);
        return Result.success("分类添加成功", null);
    }

    @PutMapping("/categories/{id}")
    public Result<Void> updateCategory(@PathVariable Long id, @RequestBody BookCategory category) {
        category.setId(id);
        libraryService.updateCategory(category);
        return Result.success("分类更新成功", null);
    }

    @DeleteMapping("/categories/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        libraryService.deleteCategory(id);
        return Result.success("分类删除成功", null);
    }

    @PostMapping("/borrow/apply")
    public Result<Void> applyBorrow(@RequestBody BorrowApplyDTO request) {
        libraryService.applyBorrow(request.getUserId(), request.getBookId(), request.getRemark());
        return Result.success("借阅申请已提交，等待管理员审批", null);
    }

    @PutMapping("/borrow/{id}/approve")
    public Result<Void> approveBorrow(@PathVariable Long id, @RequestBody(required = false) BorrowHandleDTO request) {
        libraryService.approveBorrow(id, request == null ? null : request.getRemark());
        return Result.success("审批通过，库存已扣减", null);
    }

    @PutMapping("/borrow/{id}/reject")
    public Result<Void> rejectBorrow(@PathVariable Long id, @RequestBody(required = false) BorrowHandleDTO request) {
        libraryService.rejectBorrow(id, request == null ? null : request.getRemark());
        return Result.success("已驳回借阅申请", null);
    }

    @PutMapping("/borrow/{id}/return")
    public Result<Void> returnBook(@PathVariable Long id, @RequestBody(required = false) BorrowHandleDTO request) {
        libraryService.returnBook(id, request == null ? null : request.getRemark());
        return Result.success("归还成功，库存已恢复", null);
    }

    @GetMapping("/borrow/records")
    public Result<List<BorrowRecord>> listRecords(@RequestParam(required = false) Long userId,
                                                  @RequestParam(required = false) String status) {
        return Result.success(libraryService.records(userId, status));
    }

    @GetMapping("/dashboard")
    public Result<DashboardStatsDTO> dashboard() {
        return Result.success(libraryService.dashboard());
    }
}
