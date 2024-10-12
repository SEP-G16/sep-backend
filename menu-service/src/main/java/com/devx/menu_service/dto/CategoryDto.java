package com.devx.menu_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;

    public boolean everyFieldNull() {
        return id == null && name == null;
    }
}
