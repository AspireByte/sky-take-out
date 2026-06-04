# Repository Guidelines

## 项目结构与模块组织

```
sky-take-out/
├── sky-common/          # 共享常量、异常、工具类、Result<T>、BaseContext
├── sky-pojo/            # DTO、Entity、VO，不含业务逻辑
├── sky-server/          # Spring Boot 可执行应用
│   └── src/main/java/com/sky/
│       ├── controller/  # admin/（管理端）与 user/（用户端）
│       ├── service/     # 接口 + impl/ 实现类
│       ├── mapper/      # MyBatis 映射器（XML 在 resources/mapper/）
│       ├── config/      # Spring 配置类
│       ├── interceptor/ # JWT 拦截器
│       ├── aspect/      # AOP 切面（@AutoFill）
│       └── annotation/  # 自定义注解
└── pom.xml              # 根 POM，Spring Boot 2.7.3，多模块
```

分层调用链：`controller → service → mapper`，入参为 DTO，Mapper 返回 Entity，Service 负责 DTO↔Entity 转换。

## 构建、测试与开发命令

```bash
mvn -f pom.xml clean package -DskipTests   # 构建整个项目（跳过测试）
mvn -f sky-server/pom.xml spring-boot:run # 启动服务（dev 环境，端口 8080）
mvn test                                   # 运行全部测试
```

`application.yml` 激活 `dev` profile，数据库 / Redis / OSS 等敏感配置通过 `${sky.*}` 占位符引用，实际值在 `application-dev.yml` 中。Knife4j API 文档启动后访问 `http://localhost:8080/doc.html`。

## 编码风格与命名约定

- 包名统一在 `com.sky` 下，遵循标准 Maven 目录布局。
- 实体类使用 Lombok（`@Data`、`@Builder`），减少样板代码。
- MyBatis 映射器混合使用注解（`@Select`、`@Insert`）与 XML；分页用 PageHelper（先 `startPage()` 再调 mapper）。
- Controller 使用 `@Api`、`@ApiOperation` 标注 Knife4j/Swagger 文档。
- 统一响应体 `com.sky.result.Result<T>`，`code=1` 成功，`code=0` 失败并附带 `msg`。
- JSON 序列化使用自定义 `JacksonObjectMapper`（日期格式化、Long 转字符串），在 `WebMvcConfiguration` 中注册。

## 测试指南

项目使用 `spring-boot-starter-test`（JUnit 5）。测试类放在 `src/test/java/com/sky/test/`。当前测试覆盖较薄，新增功能需补充单元测试或集成测试。

```bash
mvn test  # 运行全部测试
```

## 提交与 PR 规范

- 提交信息使用中文，格式为简短功能描述，例如 `新增 OrderDetailMapper 接口及 XML 映射，完善订单明细批量插入功能`。
- 每个 commit 聚焦单一功能或修复。
- PR 需包含变更摘要，涉及接口变更时附上 Swagger 截图或请求/响应示例。

## 关键架构说明

- **BaseContext**：通过 `ThreadLocal<Long>` 保存当前用户 ID，由 JWT 拦截器在请求前设置，供 AOP 切面和 Service 层使用。
- **@AutoFill**：自定义注解标记 Mapper 方法；AOP 切面 `AutoFillAspect` 自动填充 `createTime` / `updateTime` / `createUser` / `updateUser`。
- **JWT 认证**：`JwtTokenAdminInterceptor` 拦截 `/admin/**`（排除登录接口），解析 token 并将用户 ID 存入 BaseContext。
- **全局异常处理**：`@RestControllerAdvice` 统一捕获 `BaseException` 子类及 `SQLIntegrityConstraintViolationException`，返回标准错误格式。
