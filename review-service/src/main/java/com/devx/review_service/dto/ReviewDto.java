package com.devx.review_service.dto;

import com.devx.review_service.enums.ReviewStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String feedback;
    private ReviewStatus status;

    public boolean hasNullFields()
    {
        return name == null || feedback == null;
    }
}
