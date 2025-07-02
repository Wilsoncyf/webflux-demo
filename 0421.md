# 响应式微博客API (Reactive Micro-blogging API)

这是一个使用 Spring WebFlux 和 R2DBC 构建的、端到端（End-to-End）全响应式的微博客API项目。该项目是作为响应式编程学习课程的最终成果，旨在演示和实践响应式编程的核心概念。

## ✨ 主要特性

- **全响应式栈**: 从Web层 (WebFlux) 到数据访问层 (R2DBC) 完全非阻塞。
- **RESTful API**: 提供获取用户推文和个性化时间线等核心功能。
- **实时时间线**: 通过 **Server-Sent Events (SSE)** 实现实时的、服务器推送的时间线更新。
- **响应式数据库访问**: 使用 R2DBC 与 H2 内存数据库进行非阻塞的异步交互。
- **关联查询与DTO**: 演示了如何使用 `@Query` 注解和 `DatabaseClient` 处理多表关联查询及结果映射。
- **API文档**: 集成了 `springdoc-openapi`，自动生成交互式的 Swagger API 文档。

## 🛠️ 技术栈

- **Java 17**
- **Spring Boot 3.x**
- **Project Reactor** / **Spring WebFlux** (响应式核心)
- **Spring Data R2DBC** (响应式数据访问)
- **H2 Database** (嵌入式内存数据库)
- **Maven** (项目管理)
- **Springdoc-openapi** (Swagger/OpenAPI 3 文档)

## 🚀 API 端点

应用启动后，可以通过以下主要端点进行交互：

| 方法 | 路径                                      | 描述                                                       |
| :--- | :---------------------------------------- | :--------------------------------------------------------- |
| `GET`  | `/api/tweets/by-user/{userId}`            | 获取指定用户发布的所有推文，以标准JSON数组一次性返回。       |
| `GET`  | `/api/tweets/timeline/{userId}`           | **[流式]** 获取指定用户的时间线 (包含其关注者的推文)，以SSE方式持续推送。 |
| `GET`  | `/hello`                                  | 一个简单的 "Hello World" 接口，返回 `Mono<String>`。         |
| `GET`  | `/stream-greetings`                       | **[流式]** 一个简单的SSE接口，每秒推送一条问候语。           |

### 📚 API 文档 (Swagger UI)

项目集成了 Swagger，启动应用后，可以通过以下地址访问交互式API文档：

[**http://localhost:8080/swagger-ui.html**](http://localhost:8080/swagger-ui.html)

## ⚡ 如何运行

1.  **克隆仓库**
    ```bash
    git clone [https://github.com/Wilsoncyf/webflux-demo.git](https://github.com/Wilsoncyf/webflux-demo.git)
    cd webflux-demo
    ```

2.  **确保环境**
    -   Java 17 或更高版本
    -   Maven 3.6 或更高版本

3.  **运行应用**
    使用项目自带的 Maven Wrapper 来运行，无需本地安装 Maven：
    ```bash
    ./mvnw spring-boot:run
    ```

4.  **访问应用**
    应用将在 `http://localhost:8080` 启动。你可以使用 `curl` 或浏览器来测试上面列出的API端点。

## 📁 项目结构说明

-   `.`
    -   `src/main/java/com/example/webfluxdemo`
        -   `config`: Spring Boot 配置类 (如 OpenAPI)。
        -   `controller`: WebFlux 的 RestController 层。
        -   `entity`: 数据实体类 (使用 Java Record) 和 DTO。
        -   `repository`: Spring Data R2DBC 的 Repository 接口。
        -   `service`: 业务逻辑层。
        -   `test`: **学习过程中的各种测试和演示代码**。
    -   `src/main/resources`
        -   `application.yaml`: 应用主配置文件。
        -   `schema.sql`: H2数据库的表结构和初始化数据脚本。

---