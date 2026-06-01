package vn.vnpt.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO nhận request tạo đơn hàng mới từ client.
 * Tách biệt hoàn toàn với Entity — không phụ thuộc JPA.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotBlank(message = "customerId không được để trống")
    @Size(max = 50, message = "customerId tối đa 50 ký tự")
    private String customerId;

    @NotNull(message = "Danh sách dịch vụ không được null")
    @Size(min = 1, message = "Phải có ít nhất 1 dịch vụ trong đơn hàng")
    private List<String> items;

    @Size(max = 500, message = "Địa chỉ tối đa 500 ký tự")
    private String shippingAddress;

    private LocalDate estimatedDelivery;

    private BigDecimal totalAmount;
}
