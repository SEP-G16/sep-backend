package com.devx.booking_service.service;

import com.devx.booking_service.exception.KeyNotFoundException;
import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.record.RoomCountByRoomType;
import com.devx.booking_service.repository.RoomCountByRoomTypeRepository;
import com.devx.booking_service.repository.RoomTypeRepository;
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

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomTypeService extends BaseService<RoomType> {

    public static Logger LOG = LoggerFactory.getLogger(RoomTypeService.class);

    private final RoomTypeRepository roomTypeRepository;

    private final RoomCountByRoomTypeRepository roomCountByRoomTypeRepository;

    @Autowired
    public RoomTypeService(RoomTypeRepository roomTypeRepository, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler, RoomCountByRoomTypeRepository roomCountByRoomTypeRepository)
    {
        super(jdbcScheduler);
        this.roomTypeRepository = roomTypeRepository;
        this.jdbcScheduler = jdbcScheduler;
        this.roomCountByRoomTypeRepository = roomCountByRoomTypeRepository;
    }

    @Override
    public ResponseEntity<Mono<RoomType>> insert(RoomType roomType)
    {
        try {
            Mono<RoomType> savedMono = Mono.fromCallable(() -> roomTypeRepository.save(roomType)).subscribeOn(jdbcScheduler);
            return new ResponseEntity<>(savedMono, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            LOG.error("Error saving room type");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Flux<RoomType>> getAll()
    {
        try{
            Flux<RoomType> allTypes = Mono.fromCallable(roomTypeRepository::findAll).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
            return new ResponseEntity<>(allTypes, HttpStatus.OK);
        }catch (Exception e){
            LOG.error("Error fetching room types");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Flux<RoomCountByRoomType>> getAvailableRoomCount(LocalDate checkinDate, LocalDate checkoutDate)
    {
        try{
            Flux<RoomCountByRoomType> flux = Mono.fromCallable(() -> roomCountByRoomTypeRepository.getAvailableRoomCount(checkinDate, checkoutDate)).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
            return ResponseEntity.ok(flux);
        }
        catch (Exception e)
        {
            LOG.error(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

}
