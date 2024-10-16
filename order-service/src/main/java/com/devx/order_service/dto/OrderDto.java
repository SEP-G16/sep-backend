package com.devx.order_service.dto;

import com.devx.order_service.enums.OrderStatus;
import com.devx.order_service.model.OrderItem;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    private String sessionId;

    public boolean hasNullFields() {
        return orderItems == null || table.everyFieldIsNull() || orderItems.stream().anyMatch(OrderItemDto::hasNullFields);
    }
}
