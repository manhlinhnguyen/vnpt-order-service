package vn.vnpt.order.dto;

import lombok.*;
import vn.vnpt.order.domain.Order;
import vn.vnpt.order.domain.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO trả về thông tin đơn hàng cho client.
 * Map từ Entity — không expose trực tiếp Entity ra ngoài.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String orderId;
    private String customerId;
    private String itemsJson;
    private String shippingAddress;
    private OrderStatus status;
    private LocalDate estimatedDelivery;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Factory method: chuyển đổi từ Entity sang DTO.
     * Copilot lab: thêm các field mapping bị thiếu ở đây.
     */
    public static OrderResponse from(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .itemsJson(order.getItemsJson())
                .shippingAddress(order.getShippingAddress())
                .status(order.getStatus())
                .estimatedDelivery(order.getEstimatedDelivery())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
