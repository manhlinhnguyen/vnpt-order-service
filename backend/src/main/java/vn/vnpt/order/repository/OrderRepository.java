package vn.vnpt.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vnpt.order.domain.Order;
import vn.vnpt.order.domain.OrderStatus;
import java.util.List;

/**
 * JPA repository cho đơn hàng.
 * Chứa khai báo query — KHÔNG chứa business logic.
 *
 * Copilot lab: thêm các query method cần thiết ở đây.
 * Ví dụ:
 *   List<Order> findByCustomerIdAndStatus(String customerId, OrderStatus status);
 *   long countByCustomerId(String customerId);
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByCustomerId(String customerId);

    List<Order> findByCustomerIdAndStatus(String customerId, OrderStatus status);

    boolean existsByCustomerIdAndStatus(String customerId, OrderStatus status);
}
