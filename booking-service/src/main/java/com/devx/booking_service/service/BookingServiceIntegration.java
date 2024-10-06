package com.devx.booking_service.service;

import com.devx.booking_service.dto.BookingDto;
import com.devx.booking_service.exception.BookingNotFoundException;
import com.devx.booking_service.exception.RoomNotFoundException;
import com.devx.booking_service.exception.RoomTypeMismatchException;
import com.devx.booking_service.exception.RoomTypeNotFoundException;
import com.devx.booking_service.model.Booking;
import com.devx.booking_service.model.Room;
import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.repository.BookingRepository;
import com.devx.booking_service.repository.RoomRepository;
import com.devx.booking_service.repository.RoomTypeRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BookingServiceIntegration {

    private final Scheduler jdbcScheduler;
    private final BookingRepository bookingRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public BookingServiceIntegration(BookingRepository bookingRepository, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler, RoomRepository roomRepository, RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    private RoomType findRoomType(RoomType roomType) {
        if (roomType.getId() != null) {
            return roomTypeRepository.findById(roomType.getId()).orElseThrow(RoomTypeNotFoundException::new);
        } else {
            return roomTypeRepository.findRoomTypeByType(roomType.getType()).orElseThrow(RoomTypeNotFoundException::new);
        }
    }

    private Room findRoom(Room room) {
        if (room.getId() != null) {
            return roomRepository.findById(room.getId()).orElseThrow(RoomNotFoundException::new);
        } else {
            return roomRepository.findByRoomNo(room.getRoomNo()).orElseThrow(RoomNotFoundException::new);
        }
    }

    @Transactional
    protected Booking addBookingInternal(Booking booking) {
        RoomType roomType = findRoomType(booking.getRoomType());
        booking.setRoomType(roomType);

        List<Room> existingRooms = new ArrayList<>();

        for (Room room : booking.getRoomList()) {
            Room existingRoom = findRoom(room);
            if (!existingRoom.getRoomType().equals(roomType)) {
                throw new RoomTypeMismatchException("Selected rooms and room type mismatches for room " + existingRoom.getRoomNo());
            }
            existingRooms.add(existingRoom);
        }
        booking.setRoomList(existingRooms);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Mono<BookingDto> addBooking(Booking booking) {
        return Mono.fromCallable(() -> {
            Booking savedBooking = addBookingInternal(booking);
            return AppUtils.BookingUtils.convertEntityToDto(savedBooking);
        }).subscribeOn(jdbcScheduler);
    }

    private List<Booking> getAllBookingsInternal() {
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        return bookingRepository.findBookingsByCheckoutDateAfter(LocalDate.now(zoneId));
    }

    public Flux<BookingDto> getAllBookings() {
        return Mono.fromCallable(() -> bookingRepository.findAll().stream().map(AppUtils.BookingUtils::convertEntityToDto).toList()).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
    }

    private void removeBookingInternal(Long bookingId) {
        Optional<Booking> existingBookingOptional = bookingRepository.findById(bookingId);
        if (existingBookingOptional.isEmpty()) {
            throw new BookingNotFoundException("Booking " + bookingId + " not found");
        }
        bookingRepository.deleteById(bookingId);
    }

    public Mono<Void> removeBooking(Long bookingId) {
        removeBookingInternal(bookingId);
        return Mono.empty();
    }
}
