package com.devx.contact_service.dto;

import com.devx.contact_service.model.TicketResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportTicketDto {
    private Long id;

    private String name;

    private String email;

    private String description;

    private List<TicketResponseDto> responses;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonIgnore
    public boolean hasNullFields() {
        return name == null || email == null || description == null;
    }

    @JsonIgnore
    public boolean isFormInvalid() {
        return name == null || name.trim().isEmpty() || email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") || description == null;
    }
}
