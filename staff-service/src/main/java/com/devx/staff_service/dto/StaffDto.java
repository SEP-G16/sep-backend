package com.devx.staff_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffDto {
    private Long id;
    private String name;
    private String role;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
    private String email;

    public boolean hasNullFields() {
        return name == null || role == null || gender == null || dateOfBirth == null || address == null || phoneNumber == null;
    }
}
