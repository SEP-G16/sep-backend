package com.devx.booking_service.service;
import com.devx.booking_service.dto.TempBookingDto;
import com.devx.booking_service.exception.RoomTypeNotFoundException;
import com.devx.booking_service.exception.TempBookingNotFoundException;
import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.model.TempBooking;
import com.devx.booking_service.repository.RoomTypeRepository;
import com.devx.booking_service.repository.TempBookingRepository;
import com.devx.booking_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Component
public class TempBookingServiceIntegration {

    private final Scheduler jdbcScheduler;
    private final TempBookingRepository tempBookingRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Autowired
    public TempBookingServiceIntegration(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, TempBookingRepository tempBookingRepository, RoomTypeRepository roomTypeRepository) {
        this.jdbcScheduler = jdbcScheduler;
        this.tempBookingRepository = tempBookingRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    private RoomType findRoomType(RoomType roomType)
    {
        if(roomType.getId() != null)
        {
            return roomTypeRepository.findById(roomType.getId()).orElseThrow(RoomTypeNotFoundException::new);
        }
        else
        {
            return roomTypeRepository.findRoomTypeByType(roomType.getType()).orElseThrow(RoomTypeNotFoundException::new);
        }
    }

    private TempBooking createReservationInternal(TempBooking tempBooking) {
        tempBooking.setRoomType(findRoomType(tempBooking.getRoomType()));
        return tempBookingRepository.save(tempBooking);
    }

    public Mono<TempBookingDto> createReservation(TempBooking tempBooking) {
        return Mono.fromCallable(() -> {
            TempBooking saved = createReservationInternal(tempBooking);
            return AppUtils.TempBookingUtils.convertEntityToDto(saved);
        }).subscribeOn(jdbcScheduler);
    }

    private List<TempBooking> getAllReservationsInternal() {
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        return tempBookingRepository.findTempBookingsByCheckinDateAfter(LocalDate.now(zoneId));
    }

    public Flux<TempBookingDto> getAllReservations() {
        return Mono.fromCallable(this::getAllReservationsInternal)
                .flatMapMany(Flux::fromIterable)
                .map(AppUtils.TempBookingUtils::convertEntityToDto)
                .subscribeOn(jdbcScheduler);
    }

    @Transactional
    protected void removeReservationInternal(Long id) {
        Optional<TempBooking> existingTempBookingOptional = tempBookingRepository.findById(id);
        if (existingTempBookingOptional.isPresent()) {
            tempBookingRepository.deleteById(id);
        } else {
            throw new TempBookingNotFoundException("TempBooking " + id + " not found");
        }
    }

    @Transactional
    public Mono<Void> removeReservation(Long id)
    {
        removeReservationInternal(id);
        return Mono.empty();
    }
}
