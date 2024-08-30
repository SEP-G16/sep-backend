package com.devx.review_service.service;

import com.devx.review_service.exception.TempReviewNotFoundException;
import com.devx.review_service.model.TempReview;
import com.devx.review_service.repository.TempReviewRepository;
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
public class TempReviewService {
    private final Scheduler jdbcScheduler;
    private final TempReviewRepository tempReviewRepository;

    public static final Logger LOG = LoggerFactory.getLogger(TempReviewService.class);

    @Autowired
    public TempReviewService(TempReviewRepository tempReviewRepository, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler) {
        this.tempReviewRepository = tempReviewRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    public ResponseEntity<Mono<TempReview>> addTempReview(TempReview tempReview) {
        try {
            Mono<TempReview> saved = Mono.fromCallable(() -> tempReviewRepository.save(tempReview)).subscribeOn(jdbcScheduler);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while saving review");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Flux<TempReview>> getAllTempReviews() {
        try {
            Flux<TempReview> tempReviews = Mono.fromCallable(() -> tempReviewRepository.findAll()).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
            return new ResponseEntity<>(tempReviews, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while fetching temp reviews");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> removeTempReview(Long id){
        try {
            Optional<TempReview> tempReviewsById = tempReviewRepository.findById(id);
            if(tempReviewsById.isPresent())
            {
                tempReviewRepository.deleteById(id);
                return ResponseEntity.ok().build();
            }
            else
            {
                throw new TempReviewNotFoundException();
            }
        }
        catch (TempReviewNotFoundException e)
        {
            LOG.error("Unable to find TempReviews with the provided id");
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e)
        {
            LOG.error(("An error occurred while deleting the temp review %s").format(id.toString()));
            return ResponseEntity.internalServerError().build();
        }
    }
}


