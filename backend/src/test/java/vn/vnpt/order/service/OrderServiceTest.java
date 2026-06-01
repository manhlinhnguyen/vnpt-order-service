package vn.vnpt.order.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.vnpt.order.domain.Order;
import vn.vnpt.order.domain.OrderStatus;
import vn.vnpt.order.dto.CreateOrderRequest;
import vn.vnpt.order.exception.CustomerNotFoundException;
import vn.vnpt.order.exception.OrderNotFoundException;
import vn.vnpt.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Unit test cho OrderService.
 *
 * Copilot lab §7.10:
 * - Thêm test cho cancelOrder() khi implement.
 * - Thêm test edge case: getOrdersByCustomer với empty list.
 * - Thêm test: confirmOrder_shouldThrow_whenStatusIsNotPending.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    // -------------------------------------------------------------------------
    // createOrder
    // -------------------------------------------------------------------------

    @Test
    void createOrder_shouldReturnOrder_whenValidRequest() {
        // Arrange
        var request = CreateOrderRequest.builder()
                .customerId("CUST-001")
                .items(List.of("GOI_INTERNET_100M", "GOI_TRUYEN_HINH"))
                .shippingAddress("123 Hoàng Quốc Việt, Hà Nội")
                .totalAmount(new BigDecimal("350000.00"))
                .build();

        var savedOrder = Order.builder()
                .orderId("ORD-ABCDE123")
                .customerId("CUST-001")
                .itemsJson("GOI_INTERNET_100M,GOI_TRUYEN_HINH")
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("350000.00"))
                .build();

        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        // Act
        var result = orderService.createOrder(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo("ORD-ABCDE123");
        assertThat(result.getCustomerId()).isEqualTo("CUST-001");
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_shouldThrowCustomerNotFoundException_whenCustomerIdIsInvalid() {
        // Arrange
        var request = CreateOrderRequest.builder()
                .customerId("INVALID_CUST-999")
                .items(List.of("GOI_INTERNET_100M"))
                .build();

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("INVALID_CUST-999");

        verify(orderRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // getOrderById
    // -------------------------------------------------------------------------

    @Test
    void getOrderById_shouldReturnOrder_whenOrderExists() {
        // Arrange
        var order = Order.builder()
                .orderId("ORD-001")
                .customerId("CUST-001")
                .status(OrderStatus.CONFIRMED)
                .build();

        given(orderRepository.findById("ORD-001")).willReturn(Optional.of(order));

        // Act
        var result = orderService.getOrderById("ORD-001");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo("ORD-001");
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    void getOrderById_shouldThrowOrderNotFoundException_whenOrderDoesNotExist() {
        // Arrange
        given(orderRepository.findById("NON-EXISTENT")).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> orderService.getOrderById("NON-EXISTENT"))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("NON-EXISTENT");
    }

    // -------------------------------------------------------------------------
    // confirmOrder
    // -------------------------------------------------------------------------

    @Test
    void confirmOrder_shouldUpdateStatus_whenOrderIsPending() {
        // Arrange
        var order = Order.builder()
                .orderId("ORD-001")
                .customerId("CUST-001")
                .status(OrderStatus.PENDING)
                .build();

        given(orderRepository.findById("ORD-001")).willReturn(Optional.of(order));
        given(orderRepository.save(any(Order.class))).willAnswer(inv -> inv.getArgument(0));

        // Act
        var result = orderService.confirmOrder("ORD-001");

        // Assert
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    void confirmOrder_shouldThrowIllegalState_whenOrderIsAlreadyConfirmed() {
        // Arrange
        var order = Order.builder()
                .orderId("ORD-001")
                .status(OrderStatus.CONFIRMED)
                .build();

        given(orderRepository.findById("ORD-001")).willReturn(Optional.of(order));

        // Act & Assert
        assertThatThrownBy(() -> orderService.confirmOrder("ORD-001"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("CONFIRMED");
    }

    // -------------------------------------------------------------------------
    // TODO (Copilot lab): Thêm test cho cancelOrder khi implement
    // -------------------------------------------------------------------------
}
