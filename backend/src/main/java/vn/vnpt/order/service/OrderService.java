package vn.vnpt.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnpt.order.domain.Order;
import vn.vnpt.order.domain.OrderStatus;
import vn.vnpt.order.dto.CreateOrderRequest;
import vn.vnpt.order.dto.OrderResponse;
import vn.vnpt.order.exception.CustomerNotFoundException;
import vn.vnpt.order.exception.OrderNotFoundException;
import vn.vnpt.order.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý business logic đơn hàng VNPT.
 *
 * Kiến trúc: Service là layer duy nhất chứa business logic.
 * Controller gọi Service. Service gọi Repository.
 * Service KHÔNG trả về Entity — trả về DTO.
 *
 * Copilot lab §7.10: thêm method cancelOrder() và getOrdersByCustomer() theo mẫu này.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // TODO (Copilot lab): inject CustomerValidationClient để kiểm tra customerId thật sự
    // private final CustomerValidationClient customerClient;

    /**
     * Tạo đơn hàng mới.
     * Validate customerId tồn tại trước khi tạo đơn.
     *
     * @param request thông tin đơn hàng mới
     * @return OrderResponse với orderId được sinh
     * @throws CustomerNotFoundException nếu customerId không tồn tại
     */
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("action=createOrder customerId={} itemCount={}",
                request.getCustomerId(), request.getItems().size());

        validateCustomerExists(request.getCustomerId());

        var order = Order.builder()
                .customerId(request.getCustomerId())
                .itemsJson(String.join(",", request.getItems()))
                .shippingAddress(request.getShippingAddress())
                .estimatedDelivery(request.getEstimatedDelivery())
                .totalAmount(request.getTotalAmount())
                .status(OrderStatus.PENDING)
                .build();

        var saved = orderRepository.save(order);

        log.info("action=createOrder orderId={} status=SUCCESS", saved.getOrderId());
        return OrderResponse.from(saved);
    }

    /**
     * Lấy thông tin đơn hàng theo ID.
     *
     * @param orderId mã đơn hàng
     * @return OrderResponse
     * @throws OrderNotFoundException nếu không tìm thấy
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String orderId) {
        log.info("action=getOrderById orderId={}", orderId);

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return OrderResponse.from(order);
    }

    /**
     * Lấy danh sách đơn hàng của khách hàng.
     *
     * @param customerId mã khách hàng
     * @return danh sách đơn hàng
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomer(String customerId) {
        log.info("action=getOrdersByCustomer customerId={}", customerId);

        return orderRepository.findByCustomerId(customerId).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * Confirm đơn hàng: PENDING → CONFIRMED.
     *
     * Copilot lab: implement cancelOrder() theo cùng pattern.
     */
    @Transactional
    public OrderResponse confirmOrder(String orderId) {
        log.info("action=confirmOrder orderId={}", orderId);

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                "Chỉ có thể confirm đơn hàng ở trạng thái PENDING, hiện tại: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CONFIRMED);
        var saved = orderRepository.save(order);

        log.info("action=confirmOrder orderId={} newStatus={}", orderId, saved.getStatus());
        return OrderResponse.from(saved);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Validate customerId tồn tại trong hệ thống VNPT.
     *
     * Hiện tại: stub — luôn pass trừ customerId bắt đầu bằng "INVALID_".
     * Production: gọi Customer Management Service qua REST hoặc message queue.
     */
    private void validateCustomerExists(String customerId) {
        // STUB: thay bằng HTTP call thật trong production
        if (customerId != null && customerId.startsWith("INVALID_")) {
            throw new CustomerNotFoundException(customerId);
        }
    }
}
