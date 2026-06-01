package vn.vnpt.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.vnpt.order.dto.CreateOrderRequest;
import vn.vnpt.order.dto.OrderResponse;
import vn.vnpt.order.service.OrderService;
import java.util.List;

/**
 * REST Controller cho đơn hàng VNPT.
 *
 * Nguyên tắc: Controller KHÔNG chứa business logic.
 * Mọi logic đều ở OrderService.
 * Controller chỉ: nhận HTTP request → validate → gọi Service → trả HTTP response.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * POST /api/v1/orders
     * Tạo đơn hàng mới.
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        var order = orderService.createOrder(request);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.getOrderId())
                .toUri();

        return ResponseEntity.created(location).body(order);
    }

    /**
     * GET /api/v1/orders/{orderId}
     * Lấy thông tin đơn hàng theo ID.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    /**
     * GET /api/v1/orders?customerId={id}
     * Lấy danh sách đơn hàng của khách hàng.
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(
            @RequestParam String customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }

    /**
     * POST /api/v1/orders/{orderId}/confirm
     * Confirm đơn hàng (PENDING → CONFIRMED).
     */
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.confirmOrder(orderId));
    }

    // Copilot lab: thêm endpoint POST /{orderId}/cancel ở đây
}
