package com.devx.table_reservation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantTableDto {
    private Long id;
    private Integer tableNo;
    private Integer chairCount;

    public boolean hasNullFields() {
        // Check that tableNo and chairCount are not null (ignore id since it's auto-generated)
        return tableNo == null || chairCount == null;
    }

}
