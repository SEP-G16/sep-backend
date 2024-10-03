package com.devx.table_reservation_service;

import com.devx.table_reservation_service.model.Reservation;
import com.devx.table_reservation_service.model.RestaurantTable;
import com.devx.table_reservation_service.repository.ReservationRepository;
import com.devx.table_reservation_service.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReservationIntegrationTests extends BaseIntegrationTestConfiguration {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationService reservationService;

    Reservation testReservation1;
    Reservation testReservation2;
    Reservation testReservation3;
    Reservation savedReservation1;
    Reservation savedReservation2;

    @BeforeEach
    void setUp() {

        // Initialize test data
        testReservation1 = new Reservation();
        testReservation1.setReservedDate(LocalDate.of(2021, 5, 15));
        testReservation1.setTimeSlotStart(12);
        testReservation1.setTimeSlotEnd(14);
        testReservation1.setPeopleCount(4);
        testReservation1.setCustomerName("John Doe");
        testReservation1.setPhoneNo("0714445632");
        testReservation1.setRestaurantTableList(restaurantTableList1());

        testReservation2 = new Reservation();
        testReservation2.setReservedDate(LocalDate.of(2021, 5, 20));
        testReservation2.setTimeSlotStart(18);
        testReservation2.setTimeSlotEnd(20);
        testReservation2.setPeopleCount(2);
        testReservation2.setCustomerName("Jane Smith");
        testReservation2.setPhoneNo("0714665632");
        testReservation2.setRestaurantTableList(restaurantTableList2());

        testReservation3 = new Reservation();
        testReservation3.setReservedDate(LocalDate.of(2021, 5, 20));
        testReservation3.setTimeSlotStart(16);
        testReservation3.setTimeSlotEnd(18);
        testReservation3.setPeopleCount(2);
        testReservation3.setCustomerName("Tom Smith");
        testReservation3.setPhoneNo("0714995632");
        testReservation3.setRestaurantTableList(restaurantTableList2());
    }

    public static List<RestaurantTable> restaurantTableList1() {
        RestaurantTable table1 = new RestaurantTable();
        table1.setTableNo(1);
        table1.setChairCount(4);

        return List.of(table1);
    }

    public static List<RestaurantTable> restaurantTableList2() {
        RestaurantTable table2 = new RestaurantTable();
        table2.setTableNo(2);
        table2.setChairCount(2);

        return List.of(table2);
    }


    @Test
    public void testAddReservation() {

        Reservation savedReservation1 = reservationRepository.save(testReservation1);
        Reservation savedReservation2 = reservationRepository.save(testReservation2);

        assert savedReservation1.getId() != null;
        assert savedReservation2.getId() != null;

        assert savedReservation1.getReservedDate().equals(testReservation1.getReservedDate());
        assert savedReservation1.getTimeSlotStart().equals(testReservation1.getTimeSlotStart());
        assert savedReservation1.getTimeSlotEnd().equals(testReservation1.getTimeSlotEnd());
        assert savedReservation1.getPeopleCount().equals(testReservation1.getPeopleCount());
        assert savedReservation1.getCustomerName().equals(testReservation1.getCustomerName());
        assert savedReservation1.getPhoneNo().equals(testReservation1.getPhoneNo());
        assert savedReservation1.getRestaurantTableList().equals(testReservation1.getRestaurantTableList());

        assert savedReservation2.getReservedDate().equals(testReservation2.getReservedDate());
        assert savedReservation2.getTimeSlotStart().equals(testReservation2.getTimeSlotStart());
        assert savedReservation2.getTimeSlotEnd().equals(testReservation2.getTimeSlotEnd());
        assert savedReservation2.getPeopleCount().equals(testReservation2.getPeopleCount());
        assert savedReservation2.getCustomerName().equals(testReservation2.getCustomerName());
        assert savedReservation2.getPhoneNo().equals(testReservation2.getPhoneNo());
        assert savedReservation2.getRestaurantTableList().equals(testReservation2.getRestaurantTableList());

    }

    @Test
    public void testGetAllReservations() {
        Reservation savedReservation1 = reservationRepository.save(testReservation1);
        Reservation savedReservation2 = reservationRepository.save(testReservation2);

        ResponseEntity<Flux<Reservation>> response = reservationService.getAllReservations();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify that the reservations are returned
        StepVerifier.create(Objects.requireNonNull(response.getBody()))
                .expectNextCount(2) // Expecting 2 reservations
                .verifyComplete();
    }

    @Test
    public void testCancelReservation() {
        Reservation savedReservation3 = reservationRepository.save(testReservation3);
        // Fetch a reservation to cancel

        Long reservationId = savedReservation3.getId();

        // Cancel the reservation
        ResponseEntity<Mono<Void>> response = reservationService.cancelReservation(reservationId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        StepVerifier.create(Mono.justOrEmpty(reservationRepository.findById(reservationId)))
                .expectComplete()  // Verify that no reservation is found
                .verify();

    }
}
