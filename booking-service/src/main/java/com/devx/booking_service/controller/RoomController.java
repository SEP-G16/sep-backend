package com.devx.booking_service.controller;

import com.devx.booking_service.dto.RoomDto;
import com.devx.booking_service.exception.NullFieldException;
import com.devx.booking_service.exception.RoomTypeNotFoundException;
import com.devx.booking_service.model.Room;
import com.devx.booking_service.record.RoomListByRoomType;
import com.devx.booking_service.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public static final Logger LOG = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<RoomDto>> addRoom(@RequestBody RoomDto roomDto) {
        try {
            return ResponseEntity.created(null).body(roomService.addRoom(roomDto));
        } catch (NullFieldException | RoomTypeNotFoundException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        }
        catch (Exception e) {
            LOG.error("Error occurred while adding room", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<RoomDto>> getAllRooms() {
        try {
            return ResponseEntity.ok().body(roomService.getAll());
        } catch (Exception e) {
            LOG.error("Error occurred while fetching all rooms", e);
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<Flux<RoomListByRoomType>> getAvailableRooms(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to, Long roomTypeId) {
        try {
            return ResponseEntity.ok().body(roomService.getAvailableRoomsByRoomType(from, to, roomTypeId));
        } catch (Exception e) {
            LOG.error("Error occurred while fetching available rooms", e);
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }
}
