package com.devx.booking_service.service;

import com.devx.booking_service.exception.NoRoomsSelectedException;
import com.devx.booking_service.model.Booking;
import com.devx.booking_service.model.Room;
import com.devx.booking_service.repository.BookingRepository;
import com.devx.booking_service.repository.RoomRepository;
import com.devx.booking_service.repository.RoomTypeRepository;
import com.devx.booking_service.repository.TempBookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class BookingService extends BaseService<Booking>{

    public static final Logger LOG = LoggerFactory.getLogger(BookingService.class);

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final TempBookingRepository tempBookingRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(
            @Qualifier("jdbcScheduler") Scheduler jdbcScheduler,
            RoomTypeRepository roomTypeRepository,
            RoomRepository roomRepository,
            TempBookingRepository tempBookingRepository,
            BookingRepository bookingRepository
    ){
        super(jdbcScheduler);
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
        this.tempBookingRepository = tempBookingRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public ResponseEntity<Mono<Booking>> insert(Booking booking) {
        try {
            if (booking.getRoomList() != null) {
                Mono<Booking> savedMono = Mono.fromCallable(() -> bookingRepository.save(booking)).subscribeOn(jdbcScheduler);
                return new ResponseEntity<>(savedMono, HttpStatus.CREATED);
            }
            else {
                throw new NoRoomsSelectedException("No rooms have been selected");
            }
        }
        catch (NoRoomsSelectedException e){
            LOG.error("No rooms have been selected");
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e)
        {
            LOG.error("Error saving booking");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Flux<Booking>> getAll() {
        try{
            Flux<Booking> allRooms = Mono.fromCallable(bookingRepository::findAll).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
            return new ResponseEntity<>(allRooms, HttpStatus.OK);
        }catch (Exception e){
            LOG.error("Error fetching all bookings");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> removeBooking(Long bookingId) {
        try {
            bookingRepository.deleteById(bookingId);
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            return ResponseEntity.internalServerError().build();
        }
    }
}
