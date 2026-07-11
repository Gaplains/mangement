# 课程管理平台（Spring Boot + Vue 单页版）

> 说明：当前仓库是课程管理平台存量代码。若课程要求禁止选择“课程管理平台”，建议在提交前向教师确认选题；本次修改优先补齐可运行性、业务闭环、权限区分、数据库脚本、README 与 AI 功能模块，以满足通用 Web 实训验收指标。

## 项目简介

本系统面向校园课程选课与教学管理场景，支持管理员、教师、学生三类角色完成用户管理、课程管理、选课退课、选课名单、成绩管理、课程评价、公告管理和 AI 智能助手等功能。

## 技术栈

- 后端：Spring Boot 2.7.18、Java 17
- ORM：MyBatis 注解 Mapper
- 数据库：MySQL 8.x（Navicat 可导入 SQL 脚本）
- 前端：Vue 2、Element UI、Axios、ECharts（静态单页，位于 `src/main/resources/static/index.html`）
- 项目管理：Git、Maven
- AI 功能：本地规则 + 简单 NLP 模拟 AI（可扩展为 OpenAI / 通义千问 / Dify / Coze API）

## 运行环境

- IDEA 2023+ 或支持 Maven 的 IDE
- JDK 17
- Maven 3.8+
- MySQL 8.x
- Navicat（用于创建数据库并导入 SQL）

## 数据库初始化

1. 打开 Navicat，连接本机 MySQL。
2. 新建查询窗口，执行：`management/Course Management Platform_sql_20251019.sql`。
3. 修改 `management/src/main/resources/application.yml` 中数据库账号密码，或设置环境变量：
   - `DB_PASSWORD=你的MySQL密码`

默认数据库名：`course_management_platform`。

## 启动步骤（IDEA）

1. IDEA 打开 `management/pom.xml` 所在 Maven 项目。
2. 等待 Maven 依赖下载完成。
3. 确认 JDK 为 17。
4. 确认 MySQL 已启动并已导入初始化 SQL。
5. 运行 `com.course.management.ManagementApplication`。
6. 浏览器访问：`http://localhost:8081/api/index.html`。如果 8081 也被占用，可在 IDEA 启动配置的 VM options 中增加 `-DSERVER_PORT=8082`，或设置环境变量 `SERVER_PORT=8082`。

也可以命令行启动：

```bash
cd management
SERVER_PORT=8081 ./mvnw spring-boot:run
```


## 常见启动问题

### Port 8080/8081 was already in use

说明本机已有程序占用端口。处理方式任选一种：

1. 关闭占用端口的进程；
2. 在 IDEA 启动配置中加入 VM options：`-DSERVER_PORT=8082`；
3. 或在系统环境变量中设置 `SERVER_PORT=8082` 后重新启动。

前端请求地址已改为 `window.location.origin + '/api'`，因此端口改成 8082 后直接访问 `http://localhost:8082/api/index.html` 即可，不需要再改前端代码。

## 测试账号

所有账号演示密码均可使用：`123456`。

| 角色 | 用户名 | 说明 |
| --- | --- | --- |
| 管理员 | `admin` | 用户管理、课程管理、公告管理 |
| 教师 | `t001` | 发布课程、查看名单、录入成绩 |
| 学生 | `s001` | 浏览课程、选课退课、查看成绩 |

## 主要功能

1. 用户注册与登录：支持学生注册、三类角色登录。
2. 权限区分：管理员、教师、学生显示不同菜单。
3. 课程管理：教师发布课程，管理员/教师查看课程，支持更新和删除校验。
4. 选课业务闭环：学生浏览课程 → 选课 → 查看我的课程 → 退课；教师查看选课名单并录入成绩。
5. 用户管理：管理员查询、编辑、禁用/启用用户。
6. 数据统计：仪表板展示课程、成绩、待办和图表。
7. 公告与评价：提供公告管理、课程评价页面和演示数据。
8. AI 智能助手：提供课程问答、课程推荐、课程摘要生成三个接口与页面。

## AI 功能说明

### 已实现模块

- 后端接口：
  - `POST /api/ai/qa`：课程/选课/成绩/权限智能问答。
  - `POST /api/ai/recommend`：根据兴趣关键词推荐课程。
  - `POST /api/ai/summary`：根据课程名称与描述生成课程摘要。
- 前端页面：登录后侧边栏进入“AI助手”。
- 实现方式：本地规则 + 简单关键词匹配 + 课程库查询。

### 输入、处理与输出

- 输入：用户问题、兴趣关键词、课程名称/简介。
- 处理：服务层识别关键词，结合课程表数据匹配相关课程，生成结构化回答。
- 输出：AI 回答、相关课程、建议提示词。

### 扩展方案

将 `AiAssistantServiceImpl` 中的本地规则替换为大模型 API 调用即可，例如 OpenAI、通义千问、Dify、Coze。建议保留当前接口 DTO，便于前后端不改动即可切换真实 AI 服务。

## AI 辅助开发记录

- 使用工具：Codex / ChatGPT 类 AI 编程助手。
- 使用环节：需求分析、代码补全、AI 模块设计、README 文档整理、编译问题排查。
- 人工修改：统一接口路径、调整字段映射、补齐课程更新逻辑、增加数据库密码环境变量配置。
- 验证方式：执行 Maven 测试和编译检查。
- 局限：当前 AI 模块为模拟实现，不依赖外部网络和账号，回答质量不如真实大模型；后续可接入 Dify 或大模型 API 提升效果。

## 小组成员分工（示例，提交前请替换）

- 成员 A：后端接口、数据库设计、用户与权限模块。
- 成员 B：前端页面、课程管理、选课流程。
- 成员 C：AI 模块、测试、README、演示视频和 PPT。
