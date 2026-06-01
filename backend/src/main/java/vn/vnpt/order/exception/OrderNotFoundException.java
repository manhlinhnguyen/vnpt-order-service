package vn.vnpt.order.exception;

/**
 * Ném khi không tìm thấy đơn hàng theo orderId.
 * Sẽ được map sang HTTP 404 qua GlobalExceptionHandler.
 */
public class OrderNotFoundException extends RuntimeException {

    private final String orderId;

    public OrderNotFoundException(String orderId) {
        super("Không tìm thấy đơn hàng với ID: " + orderId);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
