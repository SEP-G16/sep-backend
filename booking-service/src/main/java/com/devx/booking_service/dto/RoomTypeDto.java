package com.devx.booking_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeDto {
    private Long id;
    private String type;
    private double price;

    @JsonIgnore
    public boolean isEveryFieldNull(){
        return id == null && type == null;
    }
}
