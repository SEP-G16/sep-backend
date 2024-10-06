package com.devx.booking_service.repository;

import com.devx.booking_service.model.TempBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TempBookingRepository extends JpaRepository<TempBooking, Long> {
    @Query(name = "FROM temp_booking t WHERE t.room_type_id = :id")
    Optional<List<TempBooking>> findByRoomTypeId(Long id);

    List<TempBooking> findTempBookingsByCheckinDateAfter(LocalDate date);
}
