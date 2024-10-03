package com.devx.review_service.controller;

import com.devx.review_service.dto.ReviewDto;
import com.devx.review_service.exception.BadRequestException;
import com.devx.review_service.exception.NullFieldException;
import com.devx.review_service.exception.ReviewNotFoundException;
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
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<ReviewDto>> saveTempReview(@RequestBody ReviewDto reviewDto) {
        try {
            return ResponseEntity.created(null).body(reviewService.addReview(reviewDto));
        } catch (NullFieldException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<ReviewDto>> getAllReviews() {
        try {
            return ResponseEntity.ok().body(reviewService.getAllReviews());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }

    @PutMapping("/accept/{reviewId}")
    public ResponseEntity<Mono<ReviewDto>> acceptReview(@PathVariable Long reviewId) {
        try {
            if(reviewId == null)
            {
                throw new BadRequestException("Review ID is required for accepting review");
            }
            return ResponseEntity.ok().body(reviewService.acceptReview(reviewId));
        } catch (ReviewNotFoundException | BadRequestException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @PutMapping("/reject/{reviewId}")
    public ResponseEntity<Mono<ReviewDto>> rejectReview(@PathVariable Long reviewId) {
        try {
            if(reviewId == null)
            {
                throw new BadRequestException("Review ID is required for rejecting review");
            }
            return ResponseEntity.ok().body(reviewService.rejectReview(reviewId));
        } catch (ReviewNotFoundException | BadRequestException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }
}
