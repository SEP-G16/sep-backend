package com.devx.booking_service.service.integration;

import com.devx.booking_service.dto.RoomTypeDto;
import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.record.RoomCountByRoomType;
import com.devx.booking_service.repository.RoomCountByRoomTypeRepository;
import com.devx.booking_service.repository.RoomTypeRepository;
import com.devx.booking_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.LocalDate;
import java.util.List;

@Component
public class RoomTypeServiceIntegration {
    private final RoomTypeRepository roomTypeRepository;

    private final RoomCountByRoomTypeRepository roomCountByRoomTypeRepository;

    private final Scheduler jdbcScheduler;

    @Autowired
    public RoomTypeServiceIntegration(RoomTypeRepository roomTypeRepository, RoomCountByRoomTypeRepository roomCountByRoomTypeRepository, Scheduler jdbcScheduler) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomCountByRoomTypeRepository = roomCountByRoomTypeRepository;
        this.jdbcScheduler = jdbcScheduler;
    }


    private RoomType addRoomTypeInternal(RoomType roomType)
    {
        return roomTypeRepository.save(roomType);
    }

    public Mono<RoomTypeDto> addRoomType(RoomType roomType)
    {
        return Mono.fromCallable(() -> {
            RoomType savedRoomType = addRoomTypeInternal(roomType);
            return AppUtils.RoomTypeUtils.convertEntityToDto(savedRoomType);
        }).subscribeOn(jdbcScheduler);
    }

    private List<RoomType> getAllRoomTypesInternal()
    {
        return roomTypeRepository.findAll();
    }

    public Flux<RoomTypeDto> getAllRoomTypes()
    {
        return Mono.fromCallable(() -> getAllRoomTypesInternal().stream().map(AppUtils.RoomTypeUtils::convertEntityToDto).toList()).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
    }

    private List<RoomCountByRoomType> getAvailableRoomCountInternal(LocalDate checkinDate, LocalDate checkoutDate)
    {
        return roomCountByRoomTypeRepository.getAvailableRoomCount(checkinDate, checkoutDate);
    }

    public Flux<RoomCountByRoomType> getAvailableRoomCount(LocalDate checkinDate, LocalDate checkoutDate)
    {
        return Mono.fromCallable(() -> getAvailableRoomCountInternal(checkinDate, checkoutDate)).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
    }

}
