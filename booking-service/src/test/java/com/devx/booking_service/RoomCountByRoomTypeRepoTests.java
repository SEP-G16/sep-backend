package com.devx.booking_service;

import com.devx.booking_service.exception.KeyNotFoundException;
import com.devx.booking_service.exception.StoredProcedureCallException;
import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.record.RoomCountByRoomType;
import com.devx.booking_service.repository.RoomCountByRoomTypeRepository;
import com.devx.booking_service.repository.RoomTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RoomCountByRoomTypeRepoTests {

    public static Logger LOG = LoggerFactory.getLogger(RoomCountByRoomTypeRepoTests.class);

    @Autowired
    private RoomCountByRoomTypeRepository roomCountByRoomTypeRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Test
    void verifyStoredProcedureCall() throws Exception {
        List<RoomCountByRoomType> availableRooms = roomCountByRoomTypeRepository.getAvailableRoomCount(LocalDate.of(2024, 8, 24), LocalDate.of(2024, 8, 27));
        Assertions.assertNotNull(availableRooms);
        Assertions.assertFalse(availableRooms.isEmpty());
    }
}
