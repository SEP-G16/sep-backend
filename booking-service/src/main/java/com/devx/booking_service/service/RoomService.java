package com.devx.booking_service.service;
import com.devx.booking_service.dto.RoomDto;
import com.devx.booking_service.model.Room;
import com.devx.booking_service.record.RoomListByRoomType;
import com.devx.booking_service.service.integration.RoomServiceIntegration;
import com.devx.booking_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;


@Service
public class RoomService {

    private final RoomServiceIntegration roomServiceIntegration;

    @Autowired
    public RoomService(RoomServiceIntegration roomServiceIntegration) {
        this.roomServiceIntegration = roomServiceIntegration;
    }


    public Mono<RoomDto> addRoom(RoomDto roomDto) {
        Room room = AppUtils.RoomUtils.convertDtoToEntity(roomDto);
        return roomServiceIntegration.addRoom(room);
    }


    public Flux<RoomDto> getAll() {
        return roomServiceIntegration.getAllRooms();
    }

    public Flux<RoomListByRoomType> getAvailableRoomsByRoomType(LocalDate from, LocalDate to, Long roomTypeId)
    {
        return roomServiceIntegration.getAvailableRoomsByRoomType(from, to, roomTypeId);
    }
}
