package com.devx.staff_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private Long id;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonIgnore
    public boolean isEveryFiledNull() {
        return name == null;
    }

    @JsonIgnore
    public boolean invalidForm() {
        String idPattern = "^[0-9]+$";
        String namePattern = "^[\\p{L} .'-]+$";
        return id == null || !id.toString().matches(idPattern) || name == null || !name.matches(namePattern);
    }
}
