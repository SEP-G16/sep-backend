package com.devx.booking_service;

import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.model.TempBooking;
import com.devx.booking_service.repository.RoomTypeRepository;
import com.devx.booking_service.repository.TempBookingRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TempBookingRepoTests {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private TempBookingRepository tempBookingRepository;

    private RoomType roomType;

    private TempBooking tempBooking;
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

        this.tempBooking = new TempBooking();
        tempBooking.setCreatedAt(LocalDateTime.now());
        tempBooking.setCustomerName("Venura");
        tempBooking.setEmail("vks250102@gmail.com");
        tempBooking.setPhoneNo("0776990588");
        tempBooking.setRoomType(roomType);
        tempBooking.setAdultCount(2);
        tempBooking.setChildrenCount(1);
        tempBooking.setRoomCount(1);
        tempBooking.setCheckinDate(LocalDate.of(2024, 8, 13));
        tempBooking.setCheckoutDate(LocalDate.of(2024, 8, 16));
    }

    @Test
    void verifyInsertToDatabase()
    {
        roomTypeRepository.save(roomType);
        TempBooking saved = tempBookingRepository.save(tempBooking);
        tempBooking.setId(saved.getId());
        Assertions.assertEquals(tempBooking, saved, "Both should be the same");
    }

    @Test
    void verifyFetchQuery()
    {
        RoomType savedType = roomTypeRepository.save(roomType);
        tempBooking.setRoomType(savedType);

        TempBooking savedBooking = tempBookingRepository.save(tempBooking);
        tempBooking.setId(savedBooking.getId());
        Optional<List<TempBooking>> savedList = tempBookingRepository.findByRoomTypeId(savedType.getId());

        Assertions.assertTrue(savedList.isPresent());
        Assertions.assertNotNull(savedList.get());
    }
}
