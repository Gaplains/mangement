package com.course.management.service.impl;

import com.course.management.dto.library.DashboardStatsDTO;
import com.course.management.entity.Book;
import com.course.management.entity.BookCategory;
import com.course.management.entity.BorrowRecord;
import com.course.management.mapper.BookCategoryMapper;
import com.course.management.mapper.BookMapper;
import com.course.management.mapper.BorrowRecordMapper;
import com.course.management.mapper.UserMapper;
import com.course.management.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

@Service
public class LibraryServiceImpl implements LibraryService {

    private static final String STATUS_ON_SHELF = "ON_SHELF";
    private static final String STATUS_OUT_OF_STOCK = "OUT_OF_STOCK";
    private static final String RECORD_PENDING = "PENDING";
    private static final String RECORD_BORROWED = "BORROWED";
    private static final String RECORD_RETURNED = "RETURNED";

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookCategoryMapper categoryMapper;

    @Autowired
    private BorrowRecordMapper recordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Book> searchBooks(String keyword, Long categoryId, String status) {
        return bookMapper.search(trim(keyword), categoryId, normalizeStatus(status));
    }

    @Override
    public Book getBook(Long id) {
        requireId(id, "图书ID不能为空");
        Book book = bookMapper.findById(id);
        if (book == null) {
            throw new RuntimeException("图书不存在");
        }
        return book;
    }

    @Override
    public void addBook(Book book) {
        validateBook(book);
        if (bookMapper.findByIsbn(book.getIsbn()) != null) {
            throw new RuntimeException("ISBN已存在，不能重复添加");
        }
        book.setAvailableStock(book.getAvailableStock() == null ? book.getTotalStock() : book.getAvailableStock());
        validateStock(book);
        book.setStatus(resolveBookStatus(book));
        bookMapper.insert(book);
    }

    @Override
    public void updateBook(Book book) {
        requireId(book == null ? null : book.getId(), "图书ID不能为空");
        validateBook(book);
        Book old = bookMapper.findById(book.getId());
        if (old == null) {
            throw new RuntimeException("图书不存在");
        }
        Book sameIsbn = bookMapper.findByIsbn(book.getIsbn());
        if (sameIsbn != null && !sameIsbn.getId().equals(book.getId())) {
            throw new RuntimeException("ISBN已被其他图书使用");
        }
        if (book.getAvailableStock() == null) {
            int borrowed = Math.max(0, old.getTotalStock() - old.getAvailableStock());
            book.setAvailableStock(Math.max(0, book.getTotalStock() - borrowed));
        }
        validateStock(book);
        book.setStatus(resolveBookStatus(book));
        bookMapper.update(book);
    }

    @Override
    public void deleteBook(Long id) {
        requireId(id, "图书ID不能为空");
        if (recordMapper.countByBookId(id) > 0) {
            throw new RuntimeException("该图书已有借阅记录，不能物理删除，可将库存设置为0或下架");
        }
        if (bookMapper.delete(id) == 0) {
            throw new RuntimeException("图书删除失败");
        }
    }

    @Override
    public List<BookCategory> listCategories() {
        return categoryMapper.findAll();
    }

    @Override
    public BookCategory getCategory(Long id) {
        requireId(id, "分类ID不能为空");
        BookCategory category = categoryMapper.findById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        return category;
    }

    @Override
    public void addCategory(BookCategory category) {
        validateCategory(category);
        if (categoryMapper.findByName(category.getName()) != null) {
            throw new RuntimeException("分类名称已存在");
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(99);
        }
        categoryMapper.insert(category);
    }

    @Override
    public void updateCategory(BookCategory category) {
        requireId(category == null ? null : category.getId(), "分类ID不能为空");
        validateCategory(category);
        BookCategory old = categoryMapper.findById(category.getId());
        if (old == null) {
            throw new RuntimeException("分类不存在");
        }
        BookCategory sameName = categoryMapper.findByName(category.getName());
        if (sameName != null && !sameName.getId().equals(category.getId())) {
            throw new RuntimeException("分类名称已存在");
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(old.getSortOrder());
        }
        categoryMapper.update(category);
    }

    @Override
    public void deleteCategory(Long id) {
        requireId(id, "分类ID不能为空");
        if (bookMapper.countByCategoryId(id) > 0) {
            throw new RuntimeException("该分类下存在图书，不能删除");
        }
        if (categoryMapper.delete(id) == 0) {
            throw new RuntimeException("分类删除失败");
        }
    }

    @Override
    @Transactional
    public void applyBorrow(Long userId, Long bookId, String remark) {
        requireId(userId, "用户ID不能为空");
        requireId(bookId, "图书ID不能为空");
        if (userMapper.findById(userId) == null) {
            throw new RuntimeException("用户不存在");
        }
        Book book = getBook(bookId);
        if (!STATUS_ON_SHELF.equals(book.getStatus()) || book.getAvailableStock() == null || book.getAvailableStock() <= 0) {
            throw new RuntimeException("图书无可借库存");
        }
        if (recordMapper.activeCount(userId, bookId) > 0) {
            throw new RuntimeException("该用户已有这本书的未完成借阅记录");
        }
        BorrowRecord record = new BorrowRecord();
        record.setUserId(userId);
        record.setBookId(bookId);
        record.setRemark(trim(remark));
        recordMapper.insert(record);
    }

    @Override
    @Transactional
    public void approveBorrow(Long recordId, String remark) {
        BorrowRecord record = requireRecord(recordId);
        if (!RECORD_PENDING.equals(record.getStatus())) {
            throw new RuntimeException("只有待审批记录可以审批通过");
        }
        if (bookMapper.decreaseStock(record.getBookId()) == 0) {
            throw new RuntimeException("库存不足，无法审批");
        }
        if (recordMapper.approve(recordId, appendRemark(record.getRemark(), remark)) == 0) {
            throw new RuntimeException("借阅审批失败");
        }
    }

    @Override
    public void rejectBorrow(Long recordId, String remark) {
        BorrowRecord record = requireRecord(recordId);
        if (!RECORD_PENDING.equals(record.getStatus())) {
            throw new RuntimeException("只有待审批记录可以驳回");
        }
        if (recordMapper.reject(recordId, appendRemark(record.getRemark(), remark)) == 0) {
            throw new RuntimeException("借阅驳回失败");
        }
    }

    @Override
    @Transactional
    public void returnBook(Long recordId, String remark) {
        BorrowRecord record = requireRecord(recordId);
        if (!RECORD_BORROWED.equals(record.getStatus()) && !"OVERDUE".equals(record.getStatus())) {
            throw new RuntimeException("只有借阅中或逾期记录可以归还");
        }
        if (recordMapper.returnBook(recordId, appendRemark(record.getRemark(), remark)) == 0) {
            throw new RuntimeException("归还失败");
        }
        bookMapper.increaseStock(record.getBookId());
    }

    @Override
    public List<BorrowRecord> records(Long userId, String status) {
        return recordMapper.search(userId, normalizeRecordStatus(status));
    }

    @Override
    public DashboardStatsDTO dashboard() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setBookCount(bookMapper.countAll());
        stats.setCategoryCount(categoryMapper.countAll());
        stats.setReaderCount(userMapper.countByRole("STUDENT"));
        stats.setTotalStock(bookMapper.sumTotalStock());
        stats.setAvailableStock(bookMapper.sumAvailableStock());
        stats.setBorrowedStock(Math.max(0, stats.getTotalStock() - stats.getAvailableStock()));
        stats.setBorrowRecords(recordMapper.countAll());
        stats.setPendingRecords(recordMapper.countByStatus(RECORD_PENDING));
        stats.setBorrowedRecords(recordMapper.countByStatus(RECORD_BORROWED));
        stats.setReturnedRecords(recordMapper.countByStatus(RECORD_RETURNED));
        return stats;
    }

    private BorrowRecord requireRecord(Long recordId) {
        requireId(recordId, "借阅记录ID不能为空");
        BorrowRecord record = recordMapper.findById(recordId);
        if (record == null) {
            throw new RuntimeException("借阅记录不存在");
        }
        return record;
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new RuntimeException("图书信息不能为空");
        }
        if (!StringUtils.hasText(book.getIsbn())) {
            throw new RuntimeException("ISBN不能为空");
        }
        if (!StringUtils.hasText(book.getTitle())) {
            throw new RuntimeException("书名不能为空");
        }
        if (!StringUtils.hasText(book.getAuthor())) {
            throw new RuntimeException("作者不能为空");
        }
        if (book.getCategoryId() == null || categoryMapper.findById(book.getCategoryId()) == null) {
            throw new RuntimeException("请选择有效图书分类");
        }
        if (book.getTotalStock() == null || book.getTotalStock() < 0) {
            throw new RuntimeException("总库存不能为负数");
        }
        book.setIsbn(book.getIsbn().trim());
        book.setTitle(book.getTitle().trim());
        book.setAuthor(book.getAuthor().trim());
    }

    private void validateStock(Book book) {
        if (book.getAvailableStock() == null || book.getAvailableStock() < 0) {
            throw new RuntimeException("可借库存不能为负数");
        }
        if (book.getAvailableStock() > book.getTotalStock()) {
            throw new RuntimeException("可借库存不能大于总库存");
        }
    }

    private void validateCategory(BookCategory category) {
        if (category == null || !StringUtils.hasText(category.getName())) {
            throw new RuntimeException("分类名称不能为空");
        }
        category.setName(category.getName().trim());
    }

    private String resolveBookStatus(Book book) {
        return book.getAvailableStock() != null && book.getAvailableStock() > 0 ? STATUS_ON_SHELF : STATUS_OUT_OF_STOCK;
    }

    private void requireId(Long id, String message) {
        if (id == null || id <= 0) {
            throw new RuntimeException(message);
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return null;
        }
        return status.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeRecordStatus(String status) {
        return normalizeStatus(status);
    }

    private String appendRemark(String oldRemark, String newRemark) {
        if (!StringUtils.hasText(newRemark)) {
            return oldRemark;
        }
        if (!StringUtils.hasText(oldRemark)) {
            return newRemark.trim();
        }
        return oldRemark + "；" + newRemark.trim();
    }
}
