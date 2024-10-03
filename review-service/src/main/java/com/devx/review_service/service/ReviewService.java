package com.devx.review_service.service;

import com.devx.review_service.dto.ReviewDto;
import com.devx.review_service.enums.ReviewStatus;
import com.devx.review_service.model.Review;
import com.devx.review_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReviewService {

    private final ReviewServiceIntegration reviewServiceIntegration;

    @Autowired
    public ReviewService(ReviewServiceIntegration reviewServiceIntegration) {
        this.reviewServiceIntegration = reviewServiceIntegration;
    }

    public Mono<ReviewDto> addReview(ReviewDto reviewDto) {
        Review review = AppUtils.convertReviewDtoToReview(reviewDto);
        review.setStatus(ReviewStatus.Pending);
        return reviewServiceIntegration.addReview(review);
    }

    public Mono<ReviewDto> acceptReview(Long reviewId) {
        return reviewServiceIntegration.updateReviewStatus(reviewId, ReviewStatus.Approved);
    }

    public Mono<ReviewDto> rejectReview(Long reviewId) {
        return reviewServiceIntegration.updateReviewStatus(reviewId, ReviewStatus.Rejected);
    }

    public Flux<ReviewDto> getAllReviews()
    {
        return reviewServiceIntegration.getAllReviews();
    }
}
