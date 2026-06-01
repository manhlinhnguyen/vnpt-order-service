package vn.vnpt.order.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity đơn hàng/dịch vụ thuê bao VNPT.
 *
 * Lưu ý: cước viễn thông dùng BigDecimal — không dùng double/float.
 * Dùng @Getter/@Setter riêng, không dùng @Data (tránh vấn đề Hibernate LazyLoad).
 */
@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_orders_customer_id", columnList = "customer_id"),
    @Index(name = "idx_orders_status", columnList = "status"),
    @Index(name = "idx_orders_created_at", columnList = "created_at")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @Column(name = "order_id", length = 50)
    private String orderId;

    @Column(name = "customer_id", nullable = false, length = 50)
    private String customerId;

    /**
     * Danh sách dịch vụ/gói cước đặt trong đơn này.
     * Lưu dạng JSON text — đủ cho demo.
     * Production: nên dùng bảng order_items riêng.
     */
    @Column(name = "items", columnDefinition = "TEXT")
    private String itemsJson;

    @Column(name = "shipping_address", length = 500)
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "estimated_delivery")
    private LocalDate estimatedDelivery;

    /**
     * Tổng cước — dùng BigDecimal, NUMERIC(15,2) trong DB.
     */
    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (orderId == null) {
            orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
