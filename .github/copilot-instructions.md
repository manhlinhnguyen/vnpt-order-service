# VNPT Order Service — GitHub Copilot Instructions

## Dự Án

**vnpt-order-service** là microservice quản lý đơn hàng/dịch vụ thuê bao cho VNPT.
Đây là repo demo dùng trong đào tạo GitHub Copilot.

## Tech Stack

- **Backend**: Java 21 + Spring Boot 3.x + Spring Data JPA + PostgreSQL
- **Billing**: .NET 8 + ASP.NET Core + Entity Framework Core
- **Frontend**: React 18 + TypeScript + Axios
- **Database**: PostgreSQL 16
- **CI/CD**: GitLab CI/CD (standalone, không dùng GitHub repos)

## Kiến Trúc (Backend Java)

Tuân theo layered architecture nghiêm ngặt:

```
Controller → Service → Repository → Database
     ↓
   DTO ←→ Domain (Entity)
```

**Quy tắc bắt buộc:**
- Controller KHÔNG gọi Repository trực tiếp — phải qua Service.
- Service chứa business logic, KHÔNG chứa HTTP handling.
- Repository là interface JPA, KHÔNG chứa business logic.
- DTO (Request/Response) KHÔNG phải Entity — tách biệt hoàn toàn.
- Entity nằm trong `domain/`, DTO nằm trong `dto/`.

## Coding Standards

### Java
- Java 21, Spring Boot 3.x
- Nullable check: dùng `Optional<T>` thay vì trả `null`
- Exception: ném exception cụ thể (`CustomerNotFoundException`, `OrderNotFoundException`) thay vì `RuntimeException`
- Logging: dùng SLF4J `@Slf4j`, KHÔNG dùng `System.out.println`
- Log format: structured — `log.info("action={} orderId={} status={}", "createOrder", id, status)`
- API error: trả `ProblemDetail` (RFC 7807) qua `@ControllerAdvice`

### .NET (Billing Service)
- .NET 8, ASP.NET Core
- Async/await cho mọi I/O
- Dùng `ILogger<T>` injection
- Không dùng `var` khi type không rõ

### TypeScript (Frontend)
- React 18 + TypeScript strict mode
- Props phải có interface rõ ràng
- API calls qua `src/api/ordersApi.ts` — không fetch trực tiếp trong component

## Security

- KHÔNG hardcode secret, connection string, API key trong code
- Validate input ở Controller với `@Valid` và DTO constraints
- SQL: dùng JPA repository hoặc parameterized query — KHÔNG string concatenation
- Kiểm tra phân quyền trước khi trả dữ liệu nhạy cảm

## Testing

- Unit test cho mọi business logic trong Service layer
- Integration test cho endpoint quan trọng (Controller layer)
- Test naming: `methodName_shouldExpectedBehavior_whenCondition`
- Mock external dependency bằng Mockito (Java) hoặc Moq (.NET)
- Không viết test chỉ assert `assertEquals(expected, expected)` — test phải thật sự kiểm chứng

## Nghiệp Vụ VNPT

- `customerId`: mã khách hàng thuê bao VNPT — phải tồn tại trong hệ thống trước khi tạo đơn hàng
- `OrderStatus`: PENDING → CONFIRMED → PROCESSING → COMPLETED / CANCELLED
- Cước viễn thông: tính theo bậc thang, dùng `BigDecimal` — KHÔNG dùng `double/float`
- Audit log: mọi thay đổi trạng thái đơn hàng phải có timestamp và actor

## GitLab Standalone

- Remote là GitLab, không phải GitHub
- CI/CD qua `.gitlab-ci.yml`
- Branch convention: `feature/`, `bugfix/`, `hotfix/`, `release/`
- Merge Request (MR) thay vì Pull Request (PR)
