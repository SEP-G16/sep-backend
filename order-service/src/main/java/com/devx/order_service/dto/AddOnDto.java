package com.devx.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOnDto {
    private Long id;
    private String name;
    private double price;

    public boolean hasNullFields(){
        return id == null;
    }
}
