---
applyTo: "**/*Test.java"
---

# Testing Standards — VNPT Backend

## Test Naming
Format: `methodName_shouldExpectedBehavior_whenCondition`
Ví dụ:
- `createOrder_shouldReturnOrder_whenValidRequest`
- `createOrder_shouldThrowException_whenCustomerNotFound`
- `getOrderById_shouldReturnNotFound_whenOrderDoesNotExist`

## Structure (AAA Pattern)
```java
@Test
void methodName_shouldBehavior_whenCondition() {
    // Arrange
    // ... setup data và mock

    // Act
    // ... gọi method cần test

    // Assert
    // ... verify kết quả
}
```

## Mocking
- Dùng `@ExtendWith(MockitoExtension.class)` — KHÔNG dùng `@SpringBootTest` cho unit test
- Mock dependency với `@Mock`, inject với `@InjectMocks`
- Verify interaction quan trọng: `verify(mock, times(1)).method(arg)`

## Assertions
- Dùng AssertJ: `assertThat(result).isNotNull().hasFieldOrPropertyWithValue(...)`
- KHÔNG dùng JUnit `assertEquals` khi AssertJ rõ hơn
- Exception test: `assertThatThrownBy(() -> service.method()).isInstanceOf(XException.class)`

## Coverage Target
- Service layer: ≥ 80% coverage
- Happy path + at least 2 edge cases cho mỗi method
- Đừng viết test chỉ để tăng coverage số — test phải kiểm chứng business logic thật sự

## Data Setup
- Dùng builder pattern: `Order.builder().orderId("ORD-001").status(PENDING).build()`
- Test data KHÔNG phụ thuộc production database
- Dùng H2 in-memory hoặc Testcontainers cho integration test
