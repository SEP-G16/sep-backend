package com.devx.booking_service;

import com.devx.booking_service.model.Room;
import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.repository.RoomRepository;
import com.devx.booking_service.repository.RoomTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RoomRepoTests {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private RoomRepository roomRepository;

    private RoomType roomType;
    private Room room;

    @BeforeEach
    void init()
    {
        this.roomType = new RoomType();
        roomType.setType("Deluxe");
        roomType.setKeywords(Arrays.asList(new String[]{"Elegant",  "Luxurious"}));
        roomType.setFloorArea(50);
        roomType.setMaximumOccupancy(4);
        roomType.setDescription("Luxury hotel room");
        roomType.setPrice(8000.0);
        roomType.setBedCount(4);

        this.room = new Room();
        room.setRoomNo(101);
    }

    @Test
    void verifyInsertToDatabase()
    {
        RoomType savedType = roomTypeRepository.save(roomType);
        room.setRoomType(savedType);

        Room savedRoom = roomRepository.save(room);
        room.setId(savedRoom.getId());

        Assertions.assertEquals(room, savedRoom, "Expected Room and Saved Room should be the same");
    }

    @Test
    void verifyAvailabilityQuery()
    {
        RoomType savedType = roomTypeRepository.save(roomType);
        room.setRoomType(savedType);
        roomRepository.save(room);
    }

    @Test
    void verifyFIndByRoomTypeAndStatusQuery()
    {

        RoomType savedRoomType = roomTypeRepository.save(roomType);

        room.setRoomType(savedRoomType);

        roomRepository.save(room);
    }

    @Test
    void verifyUpdateQuery()
    {
        RoomType savedRoomType = roomTypeRepository.save(roomType);
        room.setRoomType(savedRoomType);
        Room saved = roomRepository.save(room);


        Room updated = roomRepository.findById(saved.getId()).get();

    }
}
