package com.devx.menu_service;

import com.devx.menu_service.exception.AddOnNotFoundException;
import com.devx.menu_service.exception.NullFieldException;
import com.devx.menu_service.model.AddOn;
import com.devx.menu_service.repository.AddOnRepository;
import com.devx.menu_service.service.AddOnService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddOnServiceUnitTests {

    @Mock
    private AddOnRepository addOnRepository;

    @Mock
    private Scheduler jdbcScheduler;

    @InjectMocks
    private AddOnService addOnService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addAddOn_Success() {
        AddOn addOn = new AddOn();
        addOn.setName("Extra Sauce");
        addOn.setPrice(1.49);

        when(addOnRepository.save(any(AddOn.class))).thenReturn(addOn);

        ResponseEntity<Mono<AddOn>> response = addOnService.addAddOn(addOn);

        StepVerifier.create(response.getBody())
                .expectNextMatches(savedAddOn -> savedAddOn.getName().equals("Extra Sauce"))
                .verifyComplete();

        verify(addOnRepository, times(1)).save(any(AddOn.class));
    }

    @Test
    void addAddOn_NullFieldException() {
        AddOn addOn = new AddOn();

        doThrow(new NullFieldException("Null field")).when(addOnRepository).save(any(AddOn.class));

        ResponseEntity<Mono<AddOn>> response = addOnService.addAddOn(addOn);

        StepVerifier.create(response.getBody())
                .expectNextMatches(savedAddOn -> savedAddOn.getName() == null)
                .verifyComplete();

        verify(addOnRepository, times(1)).save(any(AddOn.class));
    }

    @Test
    void addAddOn_AddOnNotFoundException() {
        AddOn addOn = new AddOn();
        addOn.setName("Extra Sauce");
        addOn.setPrice(1.49);

        doThrow(new AddOnNotFoundException("AddOn not found")).when(addOnRepository).save(any(AddOn.class));

        ResponseEntity<Mono<AddOn>> response = addOnService.addAddOn(addOn);

        StepVerifier.create(response.getBody())
                .expectError(AddOnNotFoundException.class)
                .verify();

        verify(addOnRepository, times(1)).save(any(AddOn.class));
    }
}