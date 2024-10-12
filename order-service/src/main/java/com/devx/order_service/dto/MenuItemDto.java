package com.devx.order_service.dto;

import com.devx.order_service.model.AddOn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDto {
    private Long id;
    private String name;
    private List<AddOnDto> addOns;

    public boolean hasNullFields()
    {
        return id == null || name == null || name.isEmpty() || name.isBlank() || (addOns != null && addOns.stream().anyMatch(AddOnDto::hasNullFields));
    }
}
