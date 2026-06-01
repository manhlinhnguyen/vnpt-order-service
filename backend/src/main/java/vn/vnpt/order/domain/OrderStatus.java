package vn.vnpt.order.domain;

/**
 * Trạng thái vòng đời của đơn hàng VNPT.
 * Transition hợp lệ:
 *   PENDING → CONFIRMED → PROCESSING → COMPLETED
 *   PENDING → CANCELLED
 *   CONFIRMED → CANCELLED
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    COMPLETED,
    CANCELLED
}
