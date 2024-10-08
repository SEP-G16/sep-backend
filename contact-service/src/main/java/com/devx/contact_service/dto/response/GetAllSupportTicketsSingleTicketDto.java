package com.devx.contact_service.dto.response;

import com.devx.contact_service.dto.SupportTicketDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllSupportTicketsSingleTicketDto {
    private Long id;

    private String name;

    private String email;

    private String description;

    private List<GetAllSupportTicketsResponseDto> responses;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

}
