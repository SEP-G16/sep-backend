package com.devx.menu_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public boolean hasNullFields() {
        return name == null || price == 0;
    }

    @JsonIgnore
    public boolean everyFieldNull() {
        return name == null && id == null;
    }
}
