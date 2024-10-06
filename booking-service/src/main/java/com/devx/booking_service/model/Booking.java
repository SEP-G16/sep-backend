package com.devx.booking_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String customerName;
    private String email;
    private String phoneNo;

    @ManyToOne
    private RoomType roomType;
    private int adultCount;
    private int childrenCount;
    private int roomCount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Room> roomList;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate checkinDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate checkoutDate;
}
