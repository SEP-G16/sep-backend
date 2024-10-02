package com.devx.review_service.utils;

import com.devx.review_service.dto.ReviewDto;
import com.devx.review_service.exception.NullFieldException;
import com.devx.review_service.model.Review;

public class AppUtils {
    private AppUtils() {
    }

    public static ReviewDto convertReviewToReviewDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getName(),
                review.getCreatedAt(),
                review.getFeedback(),
                review.getStatus()
        );
    }

    public static Review convertReviewDtoToReview(ReviewDto reviewDto)
    {
        if(reviewDto.hasNullFields())
        {
            throw new NullFieldException("ReviewDto has null fields");
        }
        return new Review(
                reviewDto.getId(),
                reviewDto.getName(),
                reviewDto.getCreatedAt(),
                reviewDto.getFeedback(),
                reviewDto.getStatus()
        );
    }
}
