package com.devx.booking_service.controller;

import com.devx.booking_service.dto.RoomTypeDto;
import com.devx.booking_service.dto.request.AddRoomTypeRequestBody;
import com.devx.booking_service.exception.NullFieldException;
import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.record.RoomCountByRoomType;
import com.devx.booking_service.service.RoomTypeService;
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
@RequestMapping("/room-type")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    public static final Logger LOG = LoggerFactory.getLogger(RoomTypeController.class);
    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<RoomTypeDto>> addRoomType(@RequestBody AddRoomTypeRequestBody roomTypeDto) {
        try {
            return ResponseEntity.created(null).body(roomTypeService.addRoomType(roomTypeDto));
        }
        catch (NullFieldException e)
        {
            return ResponseEntity.badRequest().body(Mono.error(e));
        }
        catch (Exception e) {
            LOG.error("Error occurred while adding room type", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<RoomTypeDto>> getAllRoomTypes() {
        try{
            return ResponseEntity.ok().body(roomTypeService.getAllRoomTypes());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }

    @GetMapping("/available-count")
    public ResponseEntity<Flux<RoomCountByRoomType>> getAvailableRoomCount(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            return ResponseEntity.ok().body(roomTypeService.getAvailableRoomCount(from, to));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }
}
