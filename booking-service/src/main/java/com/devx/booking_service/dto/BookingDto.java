package com.devx.booking_service.dto;

import com.devx.booking_service.model.Room;
import com.devx.booking_service.model.RoomType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDate createdAt;
    private String customerName;
    private String email;
    private String phoneNo;

    private RoomTypeDto roomType;
    private int adultCount;
    private int childrenCount;
    private int roomCount;


    private List<RoomDto> roomList;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate checkinDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate checkoutDate;

    public boolean hasNullFields()
    {
        return customerName == null || email == null || phoneNo == null || roomType == null || adultCount == 0 || roomCount == 0 || checkinDate == null || checkoutDate == null;
    }

    public boolean hasNullOrEmptyRoomList()
    {
        return roomList == null || roomList.isEmpty();
    }

    public boolean isRoomCountAndListLengthMismatch()
    {
        return roomList.size() != roomCount;
    }
}
