package com.devx.review_service.service;

import com.devx.review_service.dto.ReviewDto;
import com.devx.review_service.enums.ReviewStatus;
import com.devx.review_service.exception.ReviewNotFoundException;
import com.devx.review_service.model.Review;
import com.devx.review_service.repository.ReviewRepository;
import com.devx.review_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Optional;

@Component
public class ReviewServiceIntegration {

    private final Scheduler jdbcScheduler;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceIntegration(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, ReviewRepository reviewRepository) {
        this.jdbcScheduler = jdbcScheduler;
        this.reviewRepository = reviewRepository;
    }

    private Review addReviewInternal(Review review) {
        return reviewRepository.save(review);
    }


    public Mono<ReviewDto> addReview(Review review)
    {
        return Mono.fromCallable(() -> {
            Review savedReview = addReviewInternal(review);
            return AppUtils.convertReviewToReviewDto(savedReview);
        }).subscribeOn(jdbcScheduler);
    }

    private Review updateReviewInternal(Long id, ReviewStatus status)
    {
        Optional<Review> existingReviewOptional = reviewRepository.findById(id);
        if(existingReviewOptional.isPresent())
        {
            Review existingReview = existingReviewOptional.get();
            existingReview.setStatus(status);
            return reviewRepository.save(existingReview);
        }
        else
        {
            throw new ReviewNotFoundException("Review not found");
        }
    }

    public Mono<ReviewDto> updateReviewStatus(Long id, ReviewStatus status)
    {
        return Mono.fromCallable(() -> {
            Review updatedReview = updateReviewInternal(id, status);
            return AppUtils.convertReviewToReviewDto(updatedReview);
        }).subscribeOn(jdbcScheduler);
    }

    private List<Review> getAllReviewsInternal()
    {
        return reviewRepository.findAll();
    }

    public Flux<ReviewDto> getAllReviews()
    {
        return Mono.fromCallable(() -> getAllReviewsInternal().stream().map(AppUtils::convertReviewToReviewDto).toList()).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
    }
}
