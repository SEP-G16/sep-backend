package com.devx.staff_service.dto;

import com.devx.staff_service.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffDto {

    private Long id;
    private String name;
    private RoleDto role;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonIgnore
    public boolean hasNullFields() {
        return name == null || role == null || role.isEveryFiledNull() || gender == null || dateOfBirth == null || address == null || phoneNumber == null;
    }

    public String nullField()
    {
        if(name == null)
        {
            return "name";
        }
        if(role == null || role.isEveryFiledNull())
        {
            return "role";
        }
        if(gender == null)
        {
            return "gender";
        }
        if(dateOfBirth == null)
        {
            return "dateOfBirth";
        }
        if(address == null)
        {
            return "address";
        }
        if(phoneNumber == null)
        {
            return "phoneNumber";
        }
        else return "Data is valid";
    }

    @JsonIgnore
    public boolean invalidFrom() {
        String namePattern = "^[\\p{L} .'-]+$";
        String phonePattern = "^\\+?[0-9. ()-]{7,25}$";
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (name == null || !name.matches(namePattern)) {
            return true;
        }
        if (phoneNumber == null || !phoneNumber.matches(phonePattern)) {
            return true;
        }
        if(email != null && !email.matches(emailPattern)) {
            return true;
        }
        return role == null || role.invalidForm();
    }
}
