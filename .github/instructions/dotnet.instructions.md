---
applyTo: "billing/**/*.cs"
---

# .NET Coding Style — VNPT Billing Service

## Naming
- Class/Interface: `PascalCase` — `ReconciliationService`, `IBillingRepository`
- Method: `PascalCase` — `ReconcileAsync`, `GetBillingRecordAsync`
- Variable/parameter: `camelCase` — `billingRecord`, `customerId`
- Private field: `_camelCase` — `_logger`, `_repository`

## Async/Await
- TẤT CẢ I/O phải async: `Task<T>` hoặc `Task`, method name kết thúc `Async`
- KHÔNG dùng `.Result` hoặc `.Wait()` — tránh deadlock
- Truyền `CancellationToken` qua call chain

## Dependency Injection
- Inject qua constructor, KHÔNG dùng service locator
- Interface cho mọi service: `IReconciliationService`, `IBillingRepository`
- Lifetime: Scoped cho services DB, Singleton cho stateless utils

## Logging
- Dùng `ILogger<T>` injection
- Structured logging: `_logger.LogInformation("Reconciliation completed for {CustomerId}", customerId)`
- KHÔNG log sensitive data

## Error Handling
- Dùng `try/catch` ở boundary, ném exception cụ thể vào trong
- Global handler qua middleware — KHÔNG dùng `try/catch` wrap toàn controller
- Return `ProblemDetails` cho API error
