package com.devx.booking_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private Long id;
    private int roomNo;
    private RoomTypeDto roomType;

    public boolean isEveryFieldNull() {
        return id == null && roomNo == 0;
    }
}
