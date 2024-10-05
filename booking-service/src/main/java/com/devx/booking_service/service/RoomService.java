package com.devx.booking_service.service;
import com.devx.booking_service.dto.RoomDto;
import com.devx.booking_service.model.Room;
import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.record.RoomListByRoomType;
import com.devx.booking_service.repository.RoomListByRoomTypeRepository;
import com.devx.booking_service.repository.RoomRepository;
import com.devx.booking_service.utils.AppUtils;
import jakarta.transaction.Transactional;
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
