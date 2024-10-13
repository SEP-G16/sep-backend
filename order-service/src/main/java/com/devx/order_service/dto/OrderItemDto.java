package com.devx.order_service.dto;

import com.devx.order_service.enums.OrderItemStatus;
import com.devx.order_service.model.OrderItemAddOn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long id;
    private MenuItemDto menuItem;
    private List<OrderItemAddOnDto> addOns;
    private String additionalNotes;
    private Integer quantity;
    private OrderItemStatus status;

    public boolean hasNullFields() {
        return menuItem == null || quantity == null || menuItem.hasNullFields() || (addOns != null && addOns.stream().anyMatch(OrderItemAddOnDto::hasNullFields));
    }
}
