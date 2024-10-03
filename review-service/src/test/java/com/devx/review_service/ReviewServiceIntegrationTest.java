package com.devx.review_service;

import com.devx.review_service.model.Review;
import com.devx.review_service.model.TempReview;
import com.devx.review_service.repository.ReviewRepository;
import com.devx.review_service.repository.TempReviewRepository;
import com.devx.review_service.service.ReviewService;
import com.devx.review_service.service.TempReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewServiceIntegrationTest extends BaseIntegrationTestConfiguration {

    private final TempReviewService tempReviewService;
    private final ReviewService reviewService;
    private final TempReviewRepository tempReviewRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceIntegrationTest(TempReviewService tempReviewService, ReviewService reviewService, TempReviewRepository tempReviewRepository, ReviewRepository reviewRepository) {
        this.tempReviewService = tempReviewService;
        this.reviewService = reviewService;
        this.tempReviewRepository = tempReviewRepository;
        this.reviewRepository = reviewRepository;
    }

    @Test
    void testAddTempReviewAndConvertToReview() {
        // Step 1: Add a temporary review
        TempReview tempReview = new TempReview();
        tempReview.setName("John Doe");
        tempReview.setFeedback("Great experience!");

        // Save the temp review and block to get the result
        ResponseEntity<Mono<TempReview>> tempReviewResponse = tempReviewService.addTempReview(tempReview);
        TempReview savedTempReview = Objects.requireNonNull(tempReviewResponse.getBody()).block();

        // Validate that the temp review was saved successfully
        assertNotNull(savedTempReview);
        assertNotNull(savedTempReview.getId());

        // Step 2: Convert the temporary review to a permanent review
        ResponseEntity<Mono<Review>> reviewResponse = reviewService.saveTempReview(savedTempReview.getId());
        Review savedReview = Objects.requireNonNull(reviewResponse.getBody()).block();

        // Validate that the temp review was successfully converted into a permanent review
        assertNotNull(savedReview);
        assertEquals(savedTempReview.getId(), savedReview.getId());
        assertEquals(savedTempReview.getName(), savedReview.getName());
        assertEquals(savedTempReview.getFeedback(), savedReview.getFeedback());

        // Step 3: Verify that the temporary review was deleted
        Optional<TempReview> deletedTempReview = tempReviewRepository.findById(savedTempReview.getId());
        assertTrue(deletedTempReview.isEmpty(), "Temporary review should be deleted after conversion.");

        // Step 4: Verify the review is in the review repository
        Optional<Review> reviewFromRepo = reviewRepository.findById(savedReview.getId());
        assertTrue(reviewFromRepo.isPresent(), "Review should be saved in the review repository.");
        assertEquals(savedReview.getName(), reviewFromRepo.get().getName());
    }

    @Test
    void testGetAllTempReviews() {
        // Step 1: Add a few temporary reviews
        TempReview tempReview1 = new TempReview();
        tempReview1.setName("John Doe");
        tempReview1.setFeedback("Great service!");

        TempReview tempReview2 = new TempReview();
        tempReview2.setName("Jane Doe");
        tempReview2.setFeedback("Good food!");

        // Ensure the reviews are saved by blocking the execution until they are done
        tempReviewService.addTempReview(tempReview1).getBody().block();
        tempReviewService.addTempReview(tempReview2).getBody().block();

        // Step 2: Retrieve all temporary reviews
        ResponseEntity<Flux<TempReview>> tempReviewsResponse = tempReviewService.getAllTempReviews();
        List<TempReview> tempReviews = Objects.requireNonNull(tempReviewsResponse.getBody()).collectList().block();

        // Step 3: Validate the reviews
        assertNotNull(tempReviews);
        assertEquals(2, tempReviews.size());

        // Check if both reviews are present
        assertTrue(tempReviews.stream().anyMatch(tr -> "John Doe".equals(tr.getName()) && "Great service!".equals(tr.getFeedback())));
        assertTrue(tempReviews.stream().anyMatch(tr -> "Jane Doe".equals(tr.getName()) && "Good food!".equals(tr.getFeedback())));
    }

    @Test
    void testRemoveTempReview() {
        // Step 1: Add a temporary review
        TempReview tempReview = new TempReview();
        tempReview.setName("Mark Smith");
        tempReview.setFeedback("Awesome experience!");

        ResponseEntity<Mono<TempReview>> tempReviewResponse = tempReviewService.addTempReview(tempReview);
        TempReview savedTempReview = Objects.requireNonNull(tempReviewResponse.getBody()).block();

        // Step 2: Remove the temporary review
        ResponseEntity<Mono<Void>> removeResponse = tempReviewService.removeTempReview(savedTempReview.getId());

        // Step 3: Verify that the review is deleted successfully
        assertEquals(HttpStatus.OK, removeResponse.getStatusCode());

        // Step 4: Try to fetch the deleted review and assert it is not found
        Optional<TempReview> deletedTempReview = tempReviewRepository.findById(savedTempReview.getId());
        assertTrue(deletedTempReview.isEmpty(), "The review should have been deleted");
    }



}
