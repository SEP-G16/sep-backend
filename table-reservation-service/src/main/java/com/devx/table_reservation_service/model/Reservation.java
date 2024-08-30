package com.devx.table_reservation_service.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {
    @Id
    private Long id;
    private String customerName;
    private LocalDate reservedDate;
    private Integer peopleCount;
    private String phoneNo;
    private Integer timeSlotStart;
    private Integer timeSlotEnd;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RestaurantTable> restaurantTableList;
}
