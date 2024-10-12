package com.devx.order_service.dto;

import com.devx.order_service.enums.OrderStatus;
import com.devx.order_service.model.OrderItem;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDto {
    private Long id;
    private double totalAmount;
    private List<OrderItemDto> orderItems;
    private LocalDateTime orderTime;
    private OrderStatus status;
    private RestaurantTableDto table;

    public boolean hasNullFields() {
        return orderItems == null || table.everyFieldIsNull();
    }
}
