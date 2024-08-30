package com.devx.booking_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TempBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDate createdAt;

    private String customerName;
    private String customerNic;
    private String email;
    private String phoneNo;

    @ManyToOne
    private RoomType roomType;
    private int adultCount;
    private int childrenCount;
    private int roomCount;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate checkinDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate checkoutDate;
}
