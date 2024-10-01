package com.devx.review_service.controller;

import com.devx.review_service.model.TempReview;
import com.devx.review_service.service.TempReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/review/temp")
public class TempReviewController {

    private final TempReviewService tempReviewService;

    @Autowired
    public TempReviewController(TempReviewService tempReviewService)
    {
        this.tempReviewService = tempReviewService;
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<TempReview>> getAllReviews()
    {
        return tempReviewService.getAllTempReviews();
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<TempReview>> addTempReview(@RequestBody TempReview tempReview)
    {
        return tempReviewService.addTempReview(tempReview);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Mono<Void>> deleteTempReview(@RequestBody Long id){
        return tempReviewService.removeTempReview(id);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from TempReviewController! Nice to see you! Huththoo!";
    }
}
