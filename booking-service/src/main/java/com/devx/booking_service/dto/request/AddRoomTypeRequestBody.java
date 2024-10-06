package com.devx.booking_service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRoomTypeRequestBody {
    private Long id;
    private String type;
    private List<String> keywords;
    private int floorArea;
    private int maximumOccupancy;
    private int bedCount;
    private String description;
    private double price;

    @JsonIgnore
    public boolean hasNullFields()
    {
        return type == null || keywords == null || floorArea == 0 || maximumOccupancy == 0 || bedCount == 0 || description == null || price == 0;
    }
}
