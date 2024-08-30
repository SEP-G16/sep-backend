package com.devx.table_reservation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckTableAvailabilityDto {
    private LocalDate selectedDate;
    private int timeSlotStart;
    private int timeSlotEnd;
}
