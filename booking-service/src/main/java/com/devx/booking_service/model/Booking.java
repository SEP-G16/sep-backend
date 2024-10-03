package com.devx.booking_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDate createdAt;
    private String customerName;
    private String email;
    private String phoneNo;

    @ManyToOne
    private RoomType roomType;
    private int adultCount;
    private int childrenCount;
    private int roomCount;

    @OneToMany
    private List<Room> roomList;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate checkinDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate checkoutDate;
}
