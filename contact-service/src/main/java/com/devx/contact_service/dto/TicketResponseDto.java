package com.devx.contact_service.dto;

import com.devx.contact_service.model.SupportTicket;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDto {
    private Long id;

    private String response;

    private SupportTicketDto supportTicket;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonIgnore
    public boolean hasNullFields() {
        return response == null || response.isEmpty() || supportTicket == null || supportTicket.getId() == null;
    }

    @JsonIgnore
    public boolean isFormInvalid() {
        String idPattern = "^[0-9]+$"; // Example pattern: only digits
        return id != null && !id.toString().matches(idPattern);
    }

}
