DROP DATABASE IF EXISTS library_management_system;
CREATE DATABASE library_management_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE library_management_system;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL COMMENT 'ADMIN / STUDENT',
    email VARCHAR(100),
    phone VARCHAR(30),
    real_name VARCHAR(50),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE students (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    student_id VARCHAR(30),
    major VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE teachers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    teacher_id VARCHAR(30),
    department VARCHAR(50),
    title VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE book_categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    sort_order INT DEFAULT 99,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    isbn VARCHAR(30) UNIQUE NOT NULL,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(80) NOT NULL,
    category_id BIGINT NOT NULL,
    publisher VARCHAR(100),
    publish_year INT,
    description TEXT,
    total_stock INT NOT NULL DEFAULT 0,
    available_stock INT NOT NULL DEFAULT 0,
    location VARCHAR(100),
    status VARCHAR(30) DEFAULT 'ON_SHELF',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES book_categories(id),
    INDEX idx_books_category(category_id),
    INDEX idx_books_title(title),
    INDEX idx_books_status(status)
);

CREATE TABLE borrow_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    borrow_date DATETIME,
    due_date DATETIME,
    return_date DATETIME,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    remark VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    INDEX idx_records_user(user_id),
    INDEX idx_records_book(book_id),
    INDEX idx_records_status(status)
);

INSERT INTO users(username, password, role, email, phone, real_name, status) VALUES
('admin', '123456', 'ADMIN', 'admin@library.local', '13800000001', '系统管理员', 'ACTIVE'),
('reader', '123456', 'STUDENT', 'reader@library.local', '13800000002', '演示读者', 'ACTIVE'),
('librarian', '123456', 'ADMIN', 'lib@library.local', '13800000003', '图书管理员', 'ACTIVE'),
('alice', '123456', 'STUDENT', 'alice@library.local', '13800000004', '张同学', 'ACTIVE');

INSERT INTO students(user_id, student_id, major) VALUES
(2, 'R2026001', '计算机科学与技术'),
(4, 'R2026002', '软件工程');

INSERT INTO book_categories(name, description, sort_order) VALUES
('人工智能', 'AI、机器学习与数据智能', 1),
('Web开发', 'Java、Spring Boot、前端与数据库', 2),
('文学经典', '小说、散文与人文阅读', 3),
('历史社科', '历史、管理与社会科学', 4),
('工具效率', '学习方法、项目管理与效率工具', 5);

INSERT INTO books(isbn, title, author, category_id, publisher, publish_year, description, total_stock, available_stock, location, status) VALUES
('9787115546081', '人工智能：一种现代方法', 'Stuart Russell', 1, '人民邮电出版社', 2022, '人工智能领域经典教材，覆盖搜索、知识表示、机器学习与智能体。', 5, 5, 'A区-01', 'ON_SHELF'),
('9787111641247', 'Spring Boot 实战', 'Craig Walls', 2, '机械工业出版社', 2021, '系统介绍 Spring Boot Web 开发、数据访问、测试和部署。', 6, 6, 'B区-03', 'ON_SHELF'),
('9787111213826', '深入浅出 MySQL', '数据库团队', 2, '电子工业出版社', 2020, '适合数据库入门和进阶，包含 SQL、索引、事务与优化。', 4, 4, 'B区-05', 'ON_SHELF'),
('9787020002207', '围城', '钱锺书', 3, '人民文学出版社', 2019, '中国现代文学经典，以幽默笔触展现知识分子群像。', 3, 3, 'C区-02', 'ON_SHELF'),
('9787508699242', '人类简史', '尤瓦尔·赫拉利', 4, '中信出版社', 2018, '从认知革命、农业革命到科学革命，讲述人类社会演变。', 2, 2, 'D区-01', 'ON_SHELF'),
('9787115519009', '代码整洁之道', 'Robert C. Martin', 5, '人民邮电出版社', 2020, '讲解命名、函数、类、测试和重构，适合软件工程实践。', 2, 2, 'E区-02', 'ON_SHELF');

INSERT INTO borrow_records(user_id, book_id, borrow_date, due_date, return_date, status, remark, created_at) VALUES
(2, 4, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NULL, 'BORROWED', '演示：读者已借阅文学图书', NOW()),
(4, 2, NULL, DATE_ADD(NOW(), INTERVAL 30 DAY), NULL, 'PENDING', '演示：等待管理员审批', NOW());

UPDATE books SET available_stock = available_stock - 1 WHERE id = 4;
