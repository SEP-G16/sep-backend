package com.devx.booking_service.service.integration;

import com.devx.booking_service.dto.RoomDto;
import com.devx.booking_service.exception.RoomTypeNotFoundException;
import com.devx.booking_service.model.Room;
import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.record.RoomListByRoomType;
import com.devx.booking_service.repository.RoomListByRoomTypeRepository;
import com.devx.booking_service.repository.RoomRepository;
import com.devx.booking_service.repository.RoomTypeRepository;
import com.devx.booking_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.LocalDate;
import java.util.List;

@Component
public class RoomServiceIntegration {
    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomListByRoomTypeRepository roomListByRoomTypeRepository;
    private final Scheduler jdbcScheduler;

    @Autowired
    public RoomServiceIntegration(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, RoomRepository roomRepository, RoomListByRoomTypeRepository roomListByRoomTypeRepository, RoomTypeRepository roomTypeRepository) {
        this.roomRepository = roomRepository;
        this.roomListByRoomTypeRepository = roomListByRoomTypeRepository;
        this.jdbcScheduler = jdbcScheduler;
        this.roomTypeRepository = roomTypeRepository;
    }

    private RoomType findRoomType(RoomType roomType) {
        if (roomType.getId() != null) {
            return roomTypeRepository.findById(roomType.getId()).orElseThrow(RoomTypeNotFoundException::new);
        } else {
            return roomTypeRepository.findRoomTypeByType(roomType.getType()).orElseThrow(RoomTypeNotFoundException::new);
        }
    }

    private Room addRoomInternal(Room room) {
        RoomType roomType = findRoomType(room.getRoomType());
        room.setRoomType(roomType);
        return roomRepository.save(room);
    }

    public Mono<RoomDto> addRoom(Room room) {
        return Mono.fromCallable(() -> {
            Room savedRoom = addRoomInternal(room);
            return AppUtils.RoomUtils.convertEntityToDto(savedRoom);
        }).subscribeOn(jdbcScheduler);
    }

    private List<Room> getAllRoomsInternal() {
        return roomRepository.findAll();
    }

    public Flux<RoomDto> getAllRooms() {
        return Mono.fromCallable(() -> {
            return getAllRoomsInternal().stream().map(AppUtils.RoomUtils::convertEntityToDto).toList();
        }).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
    }

    private List<RoomListByRoomType> getAvailableRoomsByRoomTypeInternal(LocalDate from, LocalDate to, Long roomTypeId) {
        return roomListByRoomTypeRepository.getAvailableRoomList(from, to, roomTypeId);
    }

    public Flux<RoomListByRoomType> getAvailableRoomsByRoomType(LocalDate from, LocalDate to, Long roomTypeId) {
        return Mono.fromCallable(() -> getAvailableRoomsByRoomTypeInternal(from, to, roomTypeId)).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
    }
}
