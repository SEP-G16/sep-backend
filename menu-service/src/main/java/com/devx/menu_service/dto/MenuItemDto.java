package com.devx.menu_service.dto;

import com.devx.menu_service.enums.MenuItemStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String shortDescription;
    private String longDescription;
    private CategoryDto category;
    private double price;
    private List<String> ingredients;
    private List<AddOnDto> addOns;
    private String imageUrl;
    private String cuisine;
    private List<String> tags;
    private MenuItemStatus status;

    @JsonIgnore
    public boolean hasNullFields() {
        return name == null || shortDescription == null || longDescription == null || category == null || category.everyFieldNull() || price == 0 || ingredients == null || imageUrl == null || cuisine == null;
    }
}
