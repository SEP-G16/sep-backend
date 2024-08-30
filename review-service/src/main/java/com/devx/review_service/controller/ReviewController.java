package com.devx.review_service.controller;

import com.devx.review_service.model.Review;
import com.devx.review_service.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService)
    {
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<Review>> saveTempReview(@RequestBody Long tempReviewId)
    {
        return reviewService.saveTempReview(tempReviewId);
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<Review>> getAllReviews()
    {
        return reviewService.getAllReviews();
    }
}
