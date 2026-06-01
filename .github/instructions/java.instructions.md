---
applyTo: "backend/**/*.java"
---

# Java Coding Style — VNPT Backend

## Naming Conventions
- Class: `PascalCase` — `OrderService`, `CreateOrderRequest`
- Method/variable: `camelCase` — `createOrder`, `orderId`
- Constant: `UPPER_SNAKE_CASE` — `MAX_RETRY_COUNT`
- Package: `lowercase`, `vn.vnpt.order.<layer>` — ví dụ `vn.vnpt.order.service`

## Annotations
- Entity class: phải có `@Entity`, `@Table(name = "orders")`, `@Id`, `@GeneratedValue`
- Service class: `@Service`, log với `@Slf4j`
- Controller class: `@RestController`, `@RequestMapping("/api/v1/<resource>")`
- Repository: `extends JpaRepository<Entity, IdType>`

## Return Types
- Endpoint trả list: `ResponseEntity<List<OrderResponse>>`
- Endpoint trả single item: `ResponseEntity<OrderResponse>`
- Khi không tìm thấy: ném `OrderNotFoundException` — KHÔNG trả `null`
- Khi tạo mới thành công: trả `ResponseEntity.created(location).body(response)`

## Error Handling
- Dùng `@ControllerAdvice` + `ProblemDetail` (RFC 7807) cho error response
- KHÔNG để exception leak ra dạng raw stack trace
- Custom exception extends `RuntimeException`, kèm message rõ ràng

## Imports
- KHÔNG dùng wildcard import (`import java.util.*`)
- Ưu tiên specific import

## Lombok
- Dùng `@Getter`, `@Setter`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor` cho Entity/DTO
- KHÔNG dùng `@Data` trên Entity (gây vấn đề Hibernate LazyLoad)
