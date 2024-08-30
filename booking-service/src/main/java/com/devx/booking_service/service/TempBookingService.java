package com.devx.booking_service.service;

import com.devx.booking_service.model.TempBooking;
import com.devx.booking_service.repository.TempBookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class TempBookingService extends BaseService<TempBooking>{

    public static final Logger LOG = LoggerFactory.getLogger(TempBookingService.class);

    private final TempBookingRepository tempBookingRepository;

    public TempBookingService(TempBookingRepository tempBookingRepository, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler){
        super(jdbcScheduler);
        this.tempBookingRepository = tempBookingRepository;
    }

    @Override
    public ResponseEntity<Mono<TempBooking>> insert(TempBooking tempBooking) {
        try {
            Mono<TempBooking> savedMono = Mono.fromCallable(() -> tempBookingRepository.save(tempBooking)).subscribeOn(jdbcScheduler);
            return new ResponseEntity<>(savedMono, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            LOG.error("Error saving temp booking");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Flux<TempBooking>> getAll() {
        try{
            Flux<TempBooking> allTempBookings = Mono.fromCallable(tempBookingRepository::findAll).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
            return new ResponseEntity<>(allTempBookings, HttpStatus.OK);
        }catch (Exception e){
            LOG.error(String.format("Error fetching temp bookings : %s", e.toString()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> removeTempBooking(Long id)
    {
        try{
            tempBookingRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            LOG.error(String.format("Error deleting tempBooking %d : %s", id, e.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }
}
