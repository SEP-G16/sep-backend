package com.devx.booking_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempBookingDto {
    private Long id;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String customerName;
    private String email;
    private String phoneNo;

    private RoomTypeDto roomType;
    private int adultCount;
    private int childrenCount;
    private int roomCount;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate checkinDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate checkoutDate;

    public boolean hasNullFields()
    {
        return customerName == null || email == null || phoneNo == null || roomType == null || adultCount == 0 || roomCount == 0 || checkinDate == null || checkoutDate == null;
    }
}
