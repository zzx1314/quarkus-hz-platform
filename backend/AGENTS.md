# AGENTS.md - Guidelines for AI Coding Agents

This document provides guidelines for AI coding agents working on this codebase.

## Project Overview

This is a **Quarkus-based Java 21** backend application. It uses:
- **Framework**: Quarkus 3.29.3 (Jakarta EE)
- **Build Tool**: Maven (with wrapper)
- **Database**: PostgreSQL + Hibernate ORM Panache
- **Cache/Queue**: Redis
- **ORM**: MapStruct for entity mapping
- **Boilerplate Reduction**: Lombok
- **Testing**: JUnit 5 + REST-assured + Awaitility
- **AI Integration**: LangChain4j (OpenAI, Ollama)

## Build Commands

### Development
```bash
./mvnw quarkus:dev                           # Start dev mode with live coding
```

### Packaging
```bash
./mvnw package                               # Build JAR (quarkus-run.jar)
./mvnw package -Dquarkus.package.jar.type=uber-jar  # Build über-jar
./mvnw package -Dnative                      # Build native executable
./mvnw package -Dnative -Dquarkus.native.container-build=true  # Container build
```

### Running Application
```bash
java -jar target/quarkus-app/quarkus-run.jar
java -jar target/*-runner.jar                # For über-jar
./target/clever_copilot-1.0.0-SNAPSHOT-runner  # Native executable
```

### Testing
```bash
./mvnw test                                  # Run all unit tests
./mvnw test -Dtest=SystemUserControllerTest  # Run single test class
./mvnw test -Dtest=SystemUserControllerTest#testCreateUser  # Run single test method
./mvnw verify                                 # Run tests and verify
./mvnw integration-test                       # Run integration tests
```

### Code Quality
```bash
./mvnw clean                                  # Clean build artifacts
./mvnw compile                                # Compile without running tests
```

## Code Style Guidelines

### General Principles
- Follow existing patterns in the codebase
- Keep methods focused and small (prefer single responsibility)
- Use meaningful names that describe intent
- Avoid magic numbers; use constants

### Naming Conventions

| Element | Convention | Example |
|---------|------------|---------|
| Packages | lowercase, dot-separated feature modules | `org.huazhi.system.sysuser` |
| Classes | PascalCase, descriptive nouns | `SysUserService`, `PageResult` |
| Interfaces | PascalCase, often with "able" or "er" suffix | `PanacheRepository` |
| Methods | camelCase, verb-noun pattern | `listUsers()`, `selectUserPage()` |
| Variables | camelCase, meaningful names | `authToken`, `createdUserId` |
| Constants | UPPER_SNAKE_CASE | `DEFAULT_PAGE_SIZE` |
| Database tables | lowercase with underscores | `p_sys_user` |

### Import Organization

Organize imports in this order with blank lines between groups:

1. Java/Jakarta EE imports (`java.*`, `jakarta.*`)
2. Third-party framework imports (`io.quarkus.*`, `org.jboss.*`, `lombok.*`, `mapstruct.*`)
3. Application imports (`org.huazhi.*`)

```java
package org.huazhi.system.sysuser.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.huazhi.system.sysrole.entity.SysRole;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
```

### Formatting

- Indent with **4 spaces** (verify IDE settings)
- Open curly brace on same line as declaration
- Maximum line length: 120 characters
- Use blank lines to separate logical sections
- One blank line between method declarations
- Use spaces around operators: `int x = a + b;`

### Entity Design

- Use `@Data`, `@EqualsAndHashCode(callSuper = false)` with Lombok
- Extend `PanacheEntityBase` for Panache entities
- Use `@Table(name = "lowercase_with_underscores")` for table mapping
- Always include `isDeleted = 0` in queries for soft deletes
- Use `LocalDateTime` for timestamps
- Use descriptive field comments (Chinese comments acceptable)

```java
@Data
@Entity
@Table(name = "p_sys_user")
@EqualsAndHashCode(callSuper = false)
public class SysUser extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 账号
     */
    private String username;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 1 表示删除，0 表示未删除
     */
    private Integer isDeleted = 0;
}
```

### Repository Pattern

- Use `PanacheRepository<T>` for simple CRUD
- Annotate with `@ApplicationScoped`
- Use `QueryBuilder` for dynamic queries
- Return `PageResult<T>` for paginated queries

```java
@ApplicationScoped
public class SysUserRepository implements PanacheRepository<SysUser> {
    private static final Logger logger = Logger.getLogger(SysUserRepository.class);

    public PageResult<SysUser> selectUserPage(SysUserQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                        .equal("isDeleted", 0)
                        .like("username", dto.getUsername())
                        .orderBy("createTime desc");

        PanacheQuery<SysUser> query = find(qb.getQuery(), qb.getParams())
                        .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(query.list(), query.count(), pageRequest.getPage(), pageRequest.getSize());
    }
}
```

### Service Layer

- Define service as interface (`SysUserService`)
- Implementation class suffix: `*Imp` (`SysUserServiceImp`)
- Use `R<T>` for unified response types
- Keep business logic in service layer

```java
public interface SysUserService {
    R<SysUserDto> authenticate(String username, String password);
    R<Void> registerUser(SysUserDto sysUser);
}

@Service
public class SysUserServiceImp implements SysUserService {
    @Inject
    SysUserRepository userRepository;

    @Inject
    SysUserMapper userMapper;
}
```

### DTO/VO Structure

- DTOs: `entity/dto/` - for request/response data transfer
- VOs: `entity/vo/` - for view/presentation objects
- Use MapStruct `@Mapper` for entity-DTO conversion
- Query DTOs extend or include pagination params

### Error Handling

- Use `R.fail()` or `R.ok(data)` for responses
- Return `R<Void>` for operations without return data
- Log errors with appropriate level using `Logger`
- Let exceptions propagate to REST layer for handling

```java
R<SysUserDto> authenticate(String username, String password) {
    try {
        SysUser user = userRepository.find("username", username).firstResult();
        if (user == null) {
            return R.fail("用户不存在");
        }
        return R.ok(userMapper.toDto(user));
    } catch (Exception e) {
        logger.error("认证失败", e);
        return R.fail("认证失败");
    }
}
```

### REST Controllers

- Use `@Path`, `@GET`, `@POST`, `@PUT`, `@DELETE` annotations
- Return `Response.ok(R.ok(data))` or `R.ok()`
- Accept DTOs for request bodies
- Use `@Inject` or constructor injection for dependencies

```java
@Path("/sysUser")
public class SysUserController {
    @Inject
    SysUserService userService;

    @POST
    @Path("/create")
    public Response create(SysUserDto dto) {
        R<Void> result = userService.registerUser(dto);
        return Response.ok(result).build();
    }
}
```

### Testing Guidelines

- Use `@QuarkusTest` for integration tests
- Use `@TestMethodOrder(MethodOrderer.OrderAnnotation.class)` for ordered tests
- Use `REST-assured` for HTTP assertions
- Use static `@BeforeAll` setup for authentication tokens
- Use `@Order(n)` to control test execution order

```java
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemUserControllerTest {

    private static String authToken;

    @BeforeAll
    public static void setup() {
        authToken = given()
                .contentType(ContentType.URLENC)
                .formParam("grant_type", "password")
                .formParam("username", "sysadmin")
                .formParam("password", "encrypted_password")
                .when().post("/api/token")
                .then()
                .statusCode(200)
                .extract().path("access_token");
    }

    @Test
    @Order(1)
    public void testCreateUser() {
        given()
                .header("Authorization", "Bearer " + authToken)
                .when().get("/sysUser/getPage")
                .then()
                .statusCode(200)
                .body("data.records.size()", greaterThan(0));
    }
}
```

### Logger Usage

- Use `org.jboss.logging.Logger`
- Static final logger per class
- Use appropriate log levels: `debug`, `info`, `warn`, `error`

```java
private static final Logger logger = Logger.getLogger(SysUserRepository.class);

logger.info("Query params: " + dto.toString());
logger.error("Failed to fetch user", e);
```

### Dependency Injection

- Use `@Inject` for field injection (Quarkus standard)
- Use constructor injection for required dependencies
- Use `@ApplicationScoped` for services and repositories
- Use `@RequestScoped` for request-level beans

### File Structure

```
src/main/java/org/huazhi/
├── system/           # Business modules (sysuser, sysrole, sysmenu, etc.)
│   ├── sysuser/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   └── entity/
│   │       ├── dto/
│   │       ├── vo/
│   │       └── mapper/
├── util/             # Shared utilities
├── oauth/            # Authentication module
└── ai/               # AI/LLM integration
```

## IDE Configuration

- Java 21 required
- VSCode launch config available for remote debugging on port 5005
- Maven wrapper included (`mvnw`)
- Lombok and MapStruct annotation processors configured in pom.xml

## Important Notes

- Tests use hardcoded auth tokens for sysadmin; update if credentials change
- Integration tests use `@Order` for dependencies; maintain order
- Soft delete pattern: always filter by `isDeleted = 0`
- JWT authentication via `/api/token` endpoint
