# AI 辅助开发说明

## 使用的 AI 工具

- Codex / ChatGPT 类 AI 编程助手。

## AI 参与环节

- 需求拆解：对照验收要求梳理缺失功能。
- 代码生成：生成 AI 智能助手 DTO、Controller、Service。
- Bug 修复：补齐课程更新接口实际调用 Service 的逻辑。
- 文档撰写：完善 README、运行步骤、AI 功能说明。

## 典型提示词示例

1. “根据 Web 实训评分标准，为 Spring Boot 项目补齐 AI 功能模块。”
2. “为课程管理系统设计一个不依赖外部 API 的模拟 AI 课程推荐接口。”
3. “完善 README，包含 IDEA、Navicat、MySQL 启动和测试账号。”

## 人工审查与修改

- 检查接口统一返回 `Result<T>`。
- 保持 Controller、Service、Mapper 分层。
- 保留本地模拟 AI，避免外部账号或网络导致演示失败。

## 测试与验证

- 使用 Maven 执行测试与编译。
- 使用 Navicat 导入 SQL 后，可通过前端 AI 助手页面演示输入输出。

## 反思

AI 工具能快速补齐模板代码和文档，但仍需人工确认字段、业务权限、数据库脚本和本地运行环境。
