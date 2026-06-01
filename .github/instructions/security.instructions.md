---
applyTo: "**/*Controller.java, **/*Controller.cs"
---

# Security Standards — Controllers

## Input Validation
- Java: dùng `@Valid` trên `@RequestBody`, kết hợp Bean Validation (`@NotNull`, `@Size`, `@Pattern`)
- .NET: dùng `[Required]`, `[StringLength]`, `[RegularExpression]` trên DTO properties
- KHÔNG trust user input — validate tất cả trước khi xử lý

## SQL Injection Prevention
- Java: dùng JPA repository hoặc `@Query` với named parameter (`:param`) — KHÔNG string concat
- .NET: dùng EF Core LINQ hoặc parameterized query — KHÔNG `string.Format` cho SQL

## Authentication & Authorization
- Java: kiểm tra `@PreAuthorize` hoặc `SecurityContextHolder.getContext().getAuthentication()`
- .NET: `[Authorize]` hoặc `HttpContext.User.IsInRole()`
- Endpoint public phải được annotate rõ ràng — mặc định là protected

## Response Security
- KHÔNG trả nội bộ exception message, stack trace trong production response
- Dùng `ProblemDetail` (Java RFC 7807) hoặc `ProblemDetails` (.NET) cho error response
- Sensitive fields (password, token, secret) KHÔNG được log hoặc trả về client
- HTTP status code phải đúng ngữ nghĩa: 404 cho not found, 403 cho forbidden, 400 cho validation error

## Rate Limiting
- Endpoint tạo tài nguyên (`POST`) nên có rate limiting annotation hoặc ghi chú TODO

## Logging
- Log access tới sensitive resource (truy cập dữ liệu khách hàng)
- KHÔNG log PII (họ tên, số điện thoại, địa chỉ đầy đủ) — log ID thay thế
