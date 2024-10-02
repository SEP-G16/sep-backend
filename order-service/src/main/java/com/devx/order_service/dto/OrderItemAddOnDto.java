package com.devx.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemAddOnDto {
    private Long id;
    private int quantity;

    public boolean hasNullFields() {
        return id == null || quantity == 0;
    }
}
