package com.devx.table_reservation_service.dto;

import com.devx.table_reservation_service.model.RestaurantTable;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private Long id;
    private String customerName;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate reservedDate;
    private Integer peopleCount;
    private String phoneNo;
    private Integer timeSlotStart;
    private Integer timeSlotEnd;
    private List<RestaurantTableDto> restaurantTableList;

    public boolean hasNullFields(){
        return customerName == null || reservedDate == null || peopleCount == null || phoneNo == null || timeSlotStart == null || timeSlotEnd == null || restaurantTableList == null;
    }
}
