package com.devx.booking_service.controller;

import com.devx.booking_service.model.Room;
import com.devx.booking_service.record.RoomListByRoomType;
import com.devx.booking_service.service.RoomService;
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

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<Room>> addRoom(@RequestBody Room room) {
        return roomService.insert(room);
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<Room>> getAllRooms() {
        return roomService.getAll();
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<Flux<RoomListByRoomType>> getAvailableRooms(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to, Long roomTypeId) {
        return roomService.getAvailableRoomsByRoomType(from, to, roomTypeId);
    }
}
