package com.devx.menu_service.service;

import com.devx.menu_service.exception.AddOnNotFoundException;
import com.devx.menu_service.exception.BadRequestException;
import com.devx.menu_service.exception.NullFieldException;
import com.devx.menu_service.model.AddOn;
import com.devx.menu_service.repository.AddOnRepository;
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
public class AddOnService extends AddOnServiceHelper {

    public static Logger LOG = LoggerFactory.getLogger(AddOnService.class);
    private final Scheduler jdbcScheduler;

    @Autowired
    public AddOnService(AddOnRepository addOnRepository, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler) {
        super(addOnRepository);
        this.jdbcScheduler = jdbcScheduler;
    }

    public ResponseEntity<Mono<AddOn>> addAddOn(AddOn addOn) {
        try {
            AddOn savedAddOn = handleDuplicateInsert(addOn);
            return ResponseEntity.ok(Mono.just(savedAddOn).subscribeOn(jdbcScheduler));

        } catch (NullFieldException | BadRequestException e) {
            LOG.error(e.getMessage());
            return ResponseEntity.badRequest().body(Mono.just(new AddOn()));
        } catch (AddOnNotFoundException e) {
            LOG.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    public ResponseEntity<Flux<AddOn>> getAllAddOns() {
        try {
            return ResponseEntity.ok(Flux.fromIterable(addOnRepository.findAll()).subscribeOn(jdbcScheduler));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }
}
