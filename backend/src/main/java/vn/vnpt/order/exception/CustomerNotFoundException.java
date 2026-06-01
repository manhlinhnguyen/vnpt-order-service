package vn.vnpt.order.exception;

/**
 * Ném khi không tìm thấy customerId trong hệ thống VNPT.
 * Sẽ được map sang HTTP 404 qua GlobalExceptionHandler.
 */
public class CustomerNotFoundException extends RuntimeException {

    private final String customerId;

    public CustomerNotFoundException(String customerId) {
        super("Không tìm thấy khách hàng với ID: " + customerId);
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
