package com.devx.booking_service;

import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.repository.RoomTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RoomTypeRepoTests {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    private RoomType roomType;

    @BeforeEach
    public void init()
    {
        this.roomType = new RoomType();
        roomType.setType("Family");
        roomType.setKeywords(Arrays.asList(new String[]{"Spacious",  "Cozy"}));
        roomType.setFloorArea(60);
        roomType.setMaximumOccupancy(6);
        roomType.setDescription("Family Hotel Room");
        roomType.setPrice(10000.0);
    }

    @Test
    void verifyRoomType()
    {
        Assertions.assertNotNull(roomType);
        Assertions.assertEquals("Deluxe", roomType.getType(), "Correct type should be fetched");
    }

    @Test
    void insertToDatabase()
    {
        RoomType saved = roomTypeRepository.save(roomType);
        roomType.setId(saved.getId());
        Assertions.assertEquals(roomType, saved, "Database should return the same object");
        Assertions.assertArrayEquals(roomType.getKeywords().toArray(new String[0]), saved.getKeywords().toArray(new String[0]));
    }
}
