# VNPT Order Service — Demo Repo cho Đào Tạo GitHub Copilot

Đây là dự án minh họa dùng xuyên suốt tài liệu **Hướng Dẫn Thực Hành GitHub Copilot** của Microsoft Vietnam.

## Mục Đích

- Demo tất cả tính năng GHCP: completions, chat, agent mode, custom instructions, hooks, memory.
- Dùng trong workshop/lab theo từng vai trò: Developer, Tech Lead, Architect, QA, BA.
- Giữ đúng stack điển hình của VNPT: Java/Spring Boot backend, .NET 8 billing, React portal.

## Cấu Trúc Dự Án

```
vnpt-order-service/
├── .github/                          # GHCP customization — đọc cục bộ, không cần GitHub
│   ├── copilot-instructions.md       # Instruction toàn repo
│   ├── instructions/                 # Path-specific instructions
│   │   ├── java.instructions.md      # applyTo: backend/**/*.java
│   │   ├── testing.instructions.md   # applyTo: **/*Test.java
│   │   ├── dotnet.instructions.md    # applyTo: billing/**/*.cs
│   │   └── security.instructions.md  # applyTo: **/Controller.java, **/Controller.cs
│   ├── agents/
│   │   ├── code-reviewer.agent.md    # Review agent (read-only)
│   │   ├── test-writer.agent.md      # Test generation agent
│   │   └── sql-reviewer.agent.md     # SQL/schema review agent
│   ├── skills/
│   │   └── deploy-staging/
│   │       ├── SKILL.md
│   │       └── deploy.sh
│   ├── prompts/
│   │   ├── new-endpoint.prompt.md    # Scaffold endpoint mới
│   │   ├── review-checklist.prompt.md
│   │   └── db-migration.prompt.md
│   └── hooks/
│       └── pre-tool-use.json         # Chặn lệnh nguy hiểm
│
├── backend/                          # Java 21 + Spring Boot 3.x
│   ├── pom.xml
│   └── src/
│       ├── main/java/vn/vnpt/order/
│       │   ├── OrderServiceApplication.java
│       │   ├── controller/OrderController.java
│       │   ├── service/OrderService.java
│       │   ├── repository/OrderRepository.java
│       │   ├── domain/Order.java
│       │   ├── domain/OrderStatus.java
│       │   ├── dto/CreateOrderRequest.java
│       │   ├── dto/OrderResponse.java
│       │   └── exception/CustomerNotFoundException.java
│       └── test/java/vn/vnpt/order/
│           └── service/OrderServiceTest.java
│
├── billing/                          # ASP.NET 8 — đối soát cước
│   ├── BillingService.csproj
│   └── src/
│       ├── Controllers/BillingController.cs
│       ├── Services/ReconciliationService.cs
│       ├── Models/BillingRecord.cs
│       └── Program.cs
│
├── web/                              # React 18 + TypeScript — portal CSKH
│   ├── package.json
│   ├── tsconfig.json
│   └── src/
│       ├── App.tsx
│       ├── pages/OrdersPage.tsx
│       ├── components/OrderCard.tsx
│       └── api/ordersApi.ts
│
├── db/                               # PostgreSQL schema + migration
│   ├── schema.sql
│   ├── seed.sql
│   └── migrations/
│       └── V1__init_orders.sql
│
└── .gitlab-ci.yml                    # GitLab CI/CD pipeline
```

## Hướng Dẫn Sử Dụng Trong Workshop

### Lab 1 — Quick Wins (§2 trong tài liệu)
1. Mở `backend/src/main/java/vn/vnpt/order/service/OrderService.java`.
2. Thêm comment mô tả logic → quan sát ghost text.
3. Dùng `/explain` để giải thích class `OrderService`.
4. Dùng `/tests` để sinh test cho `validateOrder()`.

### Lab 2 — Agent Mode (§7 trong tài liệu)
1. Mở Chat → chọn **Agent** hoặc **Plan** mode.
2. Yêu cầu: `"Thêm endpoint GET /orders/{id} với unit test và controller test"`.
3. Review plan trước khi approve.
4. Kiểm tra diff sau khi agent chạy xong.

### Lab 3 — Context Engineering (§6 trong tài liệu)
1. Xem `.github/copilot-instructions.md` — cấu trúc layered architecture.
2. Yêu cầu Copilot thêm một feature không có trong instructions → quan sát drift.
3. Cập nhật instructions → yêu cầu lại → so sánh output.

### Lab 4 — Hooks và Policy (§7.8 trong tài liệu)
1. Xem `.github/hooks/pre-tool-use.json`.
2. Thử yêu cầu agent chạy `git push --force` → quan sát hook chặn.

## Stack

| Thành phần | Công nghệ |
|-----------|----------|
| Backend | Java 21 + Spring Boot 3.x + JPA |
| Billing | .NET 8 + ASP.NET Core |
| Frontend | React 18 + TypeScript + Axios |
| Database | PostgreSQL 16 |
| CI/CD | GitLab CI/CD |
| Test | JUnit 5 + Mockito (Java), xUnit (.NET) |

## Lưu Ý

- Repo này là **demo** — code đủ để học Copilot, không phải production.
- `.github/` đặt trong repo GitLab vẫn hoạt động với GHCP (đọc local, không cần GitHub remote).
- Xem tài liệu đào tạo đầy đủ: `raw/articles/Hướng Dẫn Thực Hành GitHub Copilot — Từ Cơ Bản Đến Nâng Cao v1.4.md`.
