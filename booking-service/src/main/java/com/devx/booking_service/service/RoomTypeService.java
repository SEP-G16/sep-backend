package com.devx.booking_service.service;

import com.devx.booking_service.dto.RoomTypeDto;
import com.devx.booking_service.exception.KeyNotFoundException;
import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.record.RoomCountByRoomType;
import com.devx.booking_service.repository.RoomCountByRoomTypeRepository;
import com.devx.booking_service.repository.RoomTypeRepository;
import com.devx.booking_service.utils.AppUtils;
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
public class RoomTypeService{

    private final RoomTypeServiceIntegration roomTypeServiceIntegration;

    @Autowired
    public RoomTypeService(RoomTypeServiceIntegration roomTypeServiceIntegration) {
        this.roomTypeServiceIntegration = roomTypeServiceIntegration;
    }


    public Mono<RoomTypeDto> addRoomType(RoomTypeDto roomTypeDto)
    {
        RoomType roomType = AppUtils.RoomTypeUtils.convertDtoToEntity(roomTypeDto);
        return roomTypeServiceIntegration.addRoomType(roomType);
    }


    public Flux<RoomTypeDto> getAllRoomTypes()
    {
        return roomTypeServiceIntegration.getAllRoomTypes();
    }

    public Flux<RoomCountByRoomType> getAvailableRoomCount(LocalDate checkinDate, LocalDate checkoutDate)
    {
        return roomTypeServiceIntegration.getAvailableRoomCount(checkinDate, checkoutDate);
    }

}
