package com.devx.review_service.service;

import com.devx.review_service.exception.TempReviewNotFoundException;
import com.devx.review_service.model.Review;
import com.devx.review_service.model.TempReview;
import com.devx.review_service.repository.ReviewRepository;
import com.devx.review_service.repository.TempReviewRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final TempReviewRepository tempReviewRepository;

    private final Scheduler jdbcScheduler;

    public static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, TempReviewRepository tempReviewRepository, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler){
        this.reviewRepository = reviewRepository;
        this.tempReviewRepository = tempReviewRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    @Transactional
    public ResponseEntity<Mono<Review>> saveTempReview(Long tempReviewId)
    {
        try {
            Optional<TempReview> tempReview = tempReviewRepository.findById(tempReviewId);
            if(tempReview.isPresent())
            {
                Mono<Review> mono = Mono.fromCallable(() -> reviewRepository.save(Review.builder()
                                .id(tempReview.get().getId())
                                .feedback(tempReview.get().getFeedback())
                                .name(tempReview.get().getName())
                        .build())).subscribeOn(jdbcScheduler);

                //deleting temp review
                tempReviewRepository.deleteById(tempReviewId);

                return new ResponseEntity<>(mono, HttpStatus.CREATED);
            }
            else {
                throw new TempReviewNotFoundException("Unable to find temporary review");
            }
        }
        catch (TempReviewNotFoundException e) {
            LOG.error("Unable to find temporary review");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            LOG.error("An unexpected error occurred while saving review");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Flux<Review>> getAllReviews(){
        try {
            Flux<Review> reviews = Mono.fromCallable(reviewRepository::findAll).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);

            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }catch (Exception e)
        {
            LOG.error("An unexpected error occurred while fetching");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
