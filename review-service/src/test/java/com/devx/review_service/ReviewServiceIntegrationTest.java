package com.devx.review_service;

import com.devx.review_service.dto.ReviewDto;
import com.devx.review_service.enums.ReviewStatus;
import com.devx.review_service.model.Review;
import com.devx.review_service.repository.ReviewRepository;
import com.devx.review_service.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewServiceIntegrationTest extends BaseIntegrationTestConfiguration {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceIntegrationTest(ReviewService reviewService, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
    }

    @Test
    void testCreateAndUpdateReviewStatus() {
        // Step 1: Create a Review entity
        Review review = new Review();
        review.setId(1L);
        review.setName("John Doe");
        review.setFeedback("Excellent service!");
        review.setStatus(ReviewStatus.Pending);

        // Step 2: Save the review entity
        reviewRepository.save(review);

        // Step 3: Verify that the review is created successfully
        assertNotNull(review.getId());  // Check that the ID is not null
        assertEquals(1L, review.getId());  // Verify the manually set ID is 1L
        assertEquals(ReviewStatus.Pending, review.getStatus());  // Verify initial status
        assertEquals("John Doe", review.getName());  // Verify the name
        assertEquals("Excellent service!", review.getFeedback());  // Verify feedback

        // Step 4: Accept the review (update its status to Approved)
        review.setStatus(ReviewStatus.Approved);  // Update status to Approved
        reviewRepository.save(review);  // Save the updated review
        Review approvedReview = reviewRepository.findById(1L).orElseThrow();  // Fetch the updated review

        // Step 5: Verify that the review status is updated to Approved
        assertEquals(ReviewStatus.Approved, approvedReview.getStatus());

        // Step 6: Reject the review (update its status to Rejected)
        approvedReview.setStatus(ReviewStatus.Rejected);  // Update status to Rejected
        reviewRepository.save(approvedReview);  // Save the updated review
        Review rejectedReview = reviewRepository.findById(1L).orElseThrow();  // Fetch the updated review

        // Step 7: Verify that the review status is updated to Rejected
        assertEquals(ReviewStatus.Rejected, rejectedReview.getStatus());
    }

    @Test
    void addReviewTest() {
        // Step 1: Create a ReviewDto object
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setName("John Doe");
        reviewDto.setFeedback("Excellent service!");

        // Step 2: Add the review using the ReviewService
        Mono<ReviewDto> reviewDtoMono = reviewService.addReview(reviewDto);

        // Step 3: Verify that the review is added successfully
        ReviewDto savedReviewDto = reviewDtoMono.block();
        assertNotNull(savedReviewDto);  // Check that the saved review is not null
        assertNotNull(savedReviewDto.getId());  // Check that the ID is not null
        assertEquals("John Doe", savedReviewDto.getName());  // Verify the name
        assertEquals("Excellent service!", savedReviewDto.getFeedback());  // Verify feedback
        assertEquals(ReviewStatus.Pending, savedReviewDto.getStatus());  // Verify initial status
    }


}
