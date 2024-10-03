package com.devx.table_reservation_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckTableAvailabilityDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate selectedDate;
    private int timeSlotStart;
    private int timeSlotEnd;

    public boolean hasNullFields()
    {
        return selectedDate == null || timeSlotStart == 0 || timeSlotEnd == 0;
    }
}
