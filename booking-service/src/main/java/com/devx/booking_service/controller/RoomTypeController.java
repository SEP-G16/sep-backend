package com.devx.booking_service.controller;

import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.record.RoomCountByRoomType;
import com.devx.booking_service.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/room-type")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<RoomType>> addRoomType(@RequestBody RoomType roomType) {
        return roomTypeService.insert(roomType);
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<RoomType>> getAllRoomTypes() {
        return roomTypeService.getAll();
    }

    @GetMapping("/available-count")
    public ResponseEntity<Flux<RoomCountByRoomType>> getAvailableRoomCount(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return roomTypeService.getAvailableRoomCount(from, to);
    }
}
