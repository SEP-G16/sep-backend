package com.devx.order_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantTableDto {
    private Long id;
    private Integer tableNo;

    @JsonIgnore
    public boolean everyFieldIsNull(){
        return id == null && tableNo == null;
    }

    @JsonIgnore
    public boolean hasNUllFields()
    {
        return id == null || tableNo == null;
    }
}
