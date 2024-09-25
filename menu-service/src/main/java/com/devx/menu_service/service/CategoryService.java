package com.devx.menu_service.service;

import com.devx.menu_service.model.Category;
import com.devx.menu_service.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class CategoryService {

    public static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final Scheduler jdbcScheduler;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository,
                           @Qualifier("jdbcScheduler")
                           Scheduler jdbcScheduler) {
        this.categoryRepository = categoryRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    public ResponseEntity<Flux<Category>> getAll() {
        try {
            return ResponseEntity.ok(Mono.fromCallable(categoryRepository::findAll).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler));
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Mono<Category>> addCategory(Category category) {
        try{
            return ResponseEntity.ok(Mono.just(categoryRepository.save(category)));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
