---
description: "Scaffold REST endpoint mới cho VNPT Order Service — backend Java + test + DTO."
---

# New Endpoint Scaffold

Tạo endpoint REST mới theo đúng layered architecture của VNPT Order Service.

## Input Cần Thiết

Trước khi bắt đầu, hỏi:
1. Tên resource/entity? (ví dụ: `Order`, `Subscription`)
2. HTTP method và path? (ví dụ: `POST /api/v1/orders`)
3. Dữ liệu input (các field)?
4. Dữ liệu output trả về?
5. Business rule cần enforce?

## Các Bước Thực Hiện

### 1. Tạo DTO

**Request DTO** tại `backend/src/main/java/vn/vnpt/order/dto/<Name>Request.java`:
```java
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class <Name>Request {
    @NotNull(message = "<field> không được null")
    private String <field>;
    // ... các field khác với validation annotation
}
```

**Response DTO** tại `backend/src/main/java/vn/vnpt/order/dto/<Name>Response.java`.

### 2. Cập Nhật Service

Thêm method vào `OrderService.java`:
- Signature rõ ràng với return type cụ thể
- Log bắt đầu và kết thúc
- Xử lý exception với custom exception class

### 3. Cập Nhật Controller

Thêm method vào `OrderController.java`:
- Annotation đúng HTTP method
- `@Valid` trên `@RequestBody`
- Trả `ResponseEntity` với status code đúng
- KHÔNG chứa business logic

### 4. Viết Test

Tạo/cập nhật test tại `backend/src/test/java/vn/vnpt/order/service/`:
- Happy path test
- Validation failure test
- Not found exception test (nếu applicable)

## Ví Dụ

```
Yêu cầu: "Thêm endpoint POST /api/v1/orders/cancel/{id} để hủy đơn hàng"

Output mong đợi:
- CancelOrderRequest.java (có reason field)
- OrderController: POST /cancel/{id} endpoint
- OrderService: cancelOrder(String orderId, CancelOrderRequest req)
- OrderServiceTest: cancelOrder_should*_when* tests
```
