package com.devx.booking_service.service;

import com.devx.booking_service.dto.RoomTypeDto;
import com.devx.booking_service.dto.request.AddRoomTypeRequestBody;
import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.record.RoomCountByRoomType;
import com.devx.booking_service.service.integration.RoomTypeServiceIntegration;
import com.devx.booking_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class RoomTypeService{

    private final RoomTypeServiceIntegration roomTypeServiceIntegration;

    @Autowired
    public RoomTypeService(RoomTypeServiceIntegration roomTypeServiceIntegration) {
        this.roomTypeServiceIntegration = roomTypeServiceIntegration;
    }


    public Mono<RoomTypeDto> addRoomType(AddRoomTypeRequestBody roomTypeDto)
    {
        RoomType roomType = AppUtils.RoomTypeUtils.convertAddRoomTypeReqBodyToEntity(roomTypeDto);
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
