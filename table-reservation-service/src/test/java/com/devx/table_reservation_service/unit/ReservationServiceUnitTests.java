package com.devx.table_reservation_service.unit;

import com.devx.table_reservation_service.service.ReservationService;
import com.devx.table_reservation_service.service.ReservationServiceIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration
@ActiveProfiles(value = "test")
public class ReservationServiceUnitTests {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationServiceIntegration reservationServiceIntegration;

    //TODO: add necessary attributes here

    @BeforeEach
    public void setup()
    {

        //TODO: define attributes here

        //TODO:add necessary mock behaviour here
    }

    //TODO: write tests
}
