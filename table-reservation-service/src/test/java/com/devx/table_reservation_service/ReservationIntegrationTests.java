package com.devx.table_reservation_service;

import com.devx.table_reservation_service.dto.ReservationDto;
import com.devx.table_reservation_service.dto.RestaurantTableDto;
import com.devx.table_reservation_service.model.RestaurantTable;
import com.devx.table_reservation_service.repository.ReservationRepository;
import com.devx.table_reservation_service.repository.TableRepository;
import com.devx.table_reservation_service.service.ReservationService;
import com.devx.table_reservation_service.utils.AppUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReservationIntegrationTests extends BaseIntegrationTestConfiguration {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    ReservationRepository reservationRepository;

    ReservationDto testReservation1;
    ReservationDto testReservation2;
    ReservationDto testReservation3;

    RestaurantTable tableEntity1;
    RestaurantTable tableEntity2;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        tableRepository.deleteAll();

        RestaurantTable table1 = new RestaurantTable();
        table1.setTableNo(1);
        table1.setChairCount(4);

        RestaurantTable table2 = new RestaurantTable();
        table2.setTableNo(2);
        table2.setChairCount(2);

        tableEntity1 = tableRepository.save(table1);
        tableEntity2 = tableRepository.save(table2);

        // Initialize test data
        testReservation1 = new ReservationDto();
        testReservation1.setReservedDate(LocalDate.of(2021, 5, 15));
        testReservation1.setTimeSlotStart(12);
        testReservation1.setTimeSlotEnd(14);
        testReservation1.setPeopleCount(4);
        testReservation1.setCustomerName("John Doe");
        testReservation1.setPhoneNo("0714445632");
        testReservation1.setRestaurantTableList(List.of(AppUtils.RestaurantTableUtils.convertRestaurantTableEntityToRestaurantTableDto(tableEntity1)));

        testReservation2 = new ReservationDto();
        testReservation2.setReservedDate(LocalDate.of(2021, 5, 20));
        testReservation2.setTimeSlotStart(18);
        testReservation2.setTimeSlotEnd(20);
        testReservation2.setPeopleCount(2);
        testReservation2.setCustomerName("Jane Smith");
        testReservation2.setPhoneNo("0714665632");
        testReservation2.setRestaurantTableList(List.of(AppUtils.RestaurantTableUtils.convertRestaurantTableEntityToRestaurantTableDto(tableEntity2)));

        testReservation3 = new ReservationDto();
        testReservation3.setReservedDate(LocalDate.of(2021, 5, 20));
        testReservation3.setTimeSlotStart(16);
        testReservation3.setTimeSlotEnd(18);
        testReservation3.setPeopleCount(2);
        testReservation3.setCustomerName("Tom Smith");
        testReservation3.setPhoneNo("0714995632");
        testReservation3.setRestaurantTableList(List.of(AppUtils.RestaurantTableUtils.convertRestaurantTableEntityToRestaurantTableDto(tableEntity1), AppUtils.RestaurantTableUtils.convertRestaurantTableEntityToRestaurantTableDto(tableEntity2)));
    }

    @Test
    public void testAddReservation() {
        // Add reservations using ReservationService
        Mono<ReservationDto> reservationPublisher1 = reservationService.addReservation(testReservation1);
        Mono<ReservationDto> reservationPublisher2 = reservationService.addReservation(testReservation2);

        // Verify that reservations are saved and have IDs assigned
        StepVerifier.create(reservationPublisher1).assertNext(reservation -> {
            assertThat(reservation.getId()).isNotNull();
            assertThat(reservation.getReservedDate()).isEqualTo(testReservation1.getReservedDate());
            assertThat(reservation.getTimeSlotStart()).isEqualTo(testReservation1.getTimeSlotStart());
            assertThat(reservation.getTimeSlotEnd()).isEqualTo(testReservation1.getTimeSlotEnd());
            assertThat(reservation.getPeopleCount()).isEqualTo(testReservation1.getPeopleCount());
            assertThat(reservation.getCustomerName()).isEqualTo(testReservation1.getCustomerName());
            assertThat(reservation.getPhoneNo()).isEqualTo(testReservation1.getPhoneNo());
        }).verifyComplete();

        StepVerifier.create(reservationPublisher2).assertNext(reservation -> {
            assertThat(reservation.getId()).isNotNull();
            assertThat(reservation.getReservedDate()).isEqualTo(testReservation2.getReservedDate());
            assertThat(reservation.getTimeSlotStart()).isEqualTo(testReservation2.getTimeSlotStart());
            assertThat(reservation.getTimeSlotEnd()).isEqualTo(testReservation2.getTimeSlotEnd());
            assertThat(reservation.getPeopleCount()).isEqualTo(testReservation2.getPeopleCount());
            assertThat(reservation.getCustomerName()).isEqualTo(testReservation2.getCustomerName());
            assertThat(reservation.getPhoneNo()).isEqualTo(testReservation2.getPhoneNo());
        }).verifyComplete();

    }

    @Test
    public void testGetAllReservations() {
        Mono<ReservationDto> reservationPublisher1 = reservationService.addReservation(testReservation1);
        Mono<ReservationDto> reservationPublisher2 = reservationService.addReservation(testReservation2);

        //block before fetching flux
        reservationPublisher1.block();
        reservationPublisher2.block();

        Flux<ReservationDto> allReservations = reservationService.getAllReservations();

        // Verify that 2 reservations were fetched and complete the test
        StepVerifier.create(allReservations).expectNextCount(2).verifyComplete();
    }


    @Test
    public void testCancelReservation() {
        // Add a reservation to be cancelled
        Mono<ReservationDto> reservationPublisher1 = reservationService.addReservation(testReservation1);
        Mono<ReservationDto> reservationPublisher2 = reservationService.addReservation(testReservation2);

        //blocking
        ReservationDto savedDto1 = reservationPublisher1.block();
        ReservationDto savedDto2 = reservationPublisher2.block();

        Flux<ReservationDto> reservationsPublisher = reservationService.getAllReservations();
        List<ReservationDto> allReservations = reservationsPublisher.collectList().block();

        // Cancel the first reservation
        assert savedDto1 != null;
        Mono<Void> cancelReservationPublisher = reservationService.cancelReservation(savedDto1.getId());
        cancelReservationPublisher.block();

        Flux<ReservationDto> updatedReservationsPublisher = reservationService.getAllReservations();
        List<ReservationDto> updatedReservations = updatedReservationsPublisher.collectList().block();

        assert allReservations != null;
        assert updatedReservations != null;
        Assertions.assertEquals(allReservations.size() - 1, updatedReservations.size());

        // Verify that the first reservation was cancelled
        StepVerifier.create(updatedReservationsPublisher).assertNext(reservation -> {
            assertThat(reservation.getId()).isNotEqualTo(savedDto1.getId());
        }).verifyComplete();
    }
}