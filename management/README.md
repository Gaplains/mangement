# 智阅图书管理系统

## 项目简介
本项目是基于 Spring Boot、MyBatis、MySQL 和原生 HTML/CSS/JavaScript 开发的 Web 高级程序设计实训项目。系统面向小型图书馆、班级图书角和阅览室，覆盖用户登录注册、图书信息管理、分类管理、借阅申请、管理员审批、归还入库、借阅记录查询、库存统计和 AI 智能服务，能够演示“管理员添加图书 → 读者查询图书 → 提交借阅申请 → 管理员审批 → 读者查看记录 → 管理员归还入库”的完整业务闭环。

## 技术栈
- 后端：Spring Boot 2.7.18、RESTful API
- ORM：MyBatis 注解 Mapper
- 数据库：MySQL 8.x
- 前端：HTML5 + CSS3 + JavaScript Fetch API
- AI：本地规则 + 简单 NLP 模拟 AI，可扩展为 OpenAI、通义千问、Dify 或 Coze 接口
- 项目管理：Git、Maven

## 运行环境
- JDK 17
- Maven 3.8+
- MySQL 8.x
- 浏览器：Chrome / Edge / Firefox

## 启动步骤
1. 创建并初始化数据库：在 MySQL 中执行 `database_library.sql`。
2. 修改数据库密码：按需设置环境变量 `DB_PASSWORD`，或编辑 `src/main/resources/application.yml`。
3. 启动后端：`mvn spring-boot:run`。
4. 访问系统：浏览器打开 `http://localhost:8081/api/index.html`。

## 测试账号
| 角色 | 用户名 | 密码 | 说明 |
| --- | --- | --- | --- |
| 管理员 | admin | 123456 | 图书、分类、借阅审批、库存统计 |
| 读者 | reader | 123456 | 查询图书、提交借阅申请、查看记录 |
| 管理员 | librarian | 123456 | 图书管理员演示账号 |

## 主要功能
1. 用户注册与登录：保留统一登录接口，返回用户角色和基础信息。
2. 图书信息增删改查：支持 ISBN、书名、作者、分类、出版社、年份、库存、馆藏位置和简介维护。
3. 图书分类管理：支持新增、编辑、删除和排序展示。
4. 图书借阅与归还：读者提交借阅申请，管理员审批通过后扣减库存，归还后恢复库存。
5. 借阅记录查询：可按用户和状态查询 PENDING、BORROWED、RETURNED、REJECTED 等记录。
6. 库存状态管理：库存扣减到 0 自动变为 OUT_OF_STOCK，归还后恢复 ON_SHELF。
7. 管理员后台：前端集成图书、分类、借阅审批和统计看板。
8. AI 功能：支持智能问答、智能推荐和智能摘要，AI 输出与图书借阅业务直接相关。

## AI 功能说明
系统实现了可演示的模拟 AI 模块：
- 智能图书推荐：根据用户输入关键词匹配书名、作者、分类和简介。
- 智能图书问答：回答借阅流程、归还规则、逾期说明和权限差异。
- 智能摘要：根据图书名称和简介生成适读说明和摘要文案。

当前实现不依赖外部账号和网络，适合课堂现场验收；未来可在 `AiAssistantServiceImpl` 中替换为大模型 HTTP API 调用。

## 小组成员分工（示例）
- 成员 A：后端接口、数据库设计、借阅流程。
- 成员 B：前端页面、交互优化、演示数据。
- 成员 C：AI 模块、文档、测试与汇报材料。

## REST API 概览
| 模块 | 方法与路径 | 说明 |
| --- | --- | --- |
| 图书 | `GET /api/library/books` | 按关键词、分类、状态查询图书 |
| 图书 | `POST /api/library/books` | 管理员新增图书 |
| 图书 | `PUT /api/library/books/{id}` | 管理员编辑图书 |
| 图书 | `DELETE /api/library/books/{id}` | 管理员删除无借阅记录图书 |
| 分类 | `GET /api/library/categories` | 查询全部分类 |
| 分类 | `POST /api/library/categories` | 新增分类 |
| 分类 | `PUT /api/library/categories/{id}` | 编辑分类 |
| 借阅 | `POST /api/library/borrow/apply` | 读者提交借阅申请 |
| 借阅 | `PUT /api/library/borrow/{id}/approve` | 管理员审批通过并扣减库存 |
| 借阅 | `PUT /api/library/borrow/{id}/reject` | 管理员驳回申请 |
| 借阅 | `PUT /api/library/borrow/{id}/return` | 管理员办理归还并恢复库存 |
| 借阅 | `GET /api/library/borrow/records` | 查询借阅记录 |
| 统计 | `GET /api/library/dashboard` | 获取库存和借阅统计 |
| AI | `POST /api/ai/qa` | 图书馆智能问答 |
| AI | `POST /api/ai/recommend` | 智能图书推荐 |
| AI | `POST /api/ai/summary` | 智能摘要生成 |
