package com.devx.menu_service.dto.request;

import com.devx.menu_service.dto.AddOnDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMenuItemRequestBody {
    private String name;
    private String longDescription;
    private String shortDescription;
    private Long categoryId;
    private double price;
    private List<String> ingredients;
    private List<AddOnDto> addOns;
    private String imageUrl;
    private String cuisine;
    private List<String> tags;

    public boolean hasNullFields() {
        return name == null ||
                longDescription == null ||
                shortDescription == null ||
                categoryId == null ||
                ingredients == null ||
                addOns == null ||
                imageUrl == null ||
                cuisine == null ||
                tags == null;
    }
}

