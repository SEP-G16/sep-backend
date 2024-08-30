package com.devx.booking_service.service;
import com.devx.booking_service.model.Room;
import com.devx.booking_service.model.RoomType;
import com.devx.booking_service.record.RoomListByRoomType;
import com.devx.booking_service.repository.RoomListByRoomTypeRepository;
import com.devx.booking_service.repository.RoomRepository;
import jakarta.transaction.Transactional;
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

import java.time.LocalDate;


@Service
public class RoomService extends BaseService<Room> {

    private final RoomRepository roomRepository;

    private final RoomListByRoomTypeRepository roomListByRoomTypeRepository;

    public static final Logger LOG = LoggerFactory.getLogger(RoomService.class);

    @Autowired
    public RoomService(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, RoomRepository roomRepository, RoomListByRoomTypeRepository roomListByRoomTypeRepository) {
        super(jdbcScheduler);
        this.roomRepository = roomRepository;
        this.roomListByRoomTypeRepository = roomListByRoomTypeRepository;
    }

    @Override
    public ResponseEntity<Mono<Room>> insert(Room room) {
        try {
            Mono<Room> savedMono = Mono.fromCallable(() -> roomRepository.save(room)).subscribeOn(jdbcScheduler);
            return new ResponseEntity<>(savedMono, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            LOG.error("Error saving room type");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Flux<Room>> getAll() {
        try{
            Flux<Room> allRooms = Mono.fromCallable(roomRepository::findAll).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
            return new ResponseEntity<>(allRooms, HttpStatus.OK);
        }catch (Exception e){
            LOG.error("Error fetching room types");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Flux<RoomListByRoomType>> getAvailableRoomsByRoomType(LocalDate from, LocalDate to, Long roomTypeId)
    {
        try{
            Flux<RoomListByRoomType> flux = Mono.fromCallable(() -> roomListByRoomTypeRepository.getAvailableRoomList(from, to, roomTypeId)).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
            return ResponseEntity.ok(flux);
        }catch (Exception e){
            LOG.error(String.format("Error getting available room list %s", e.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }
}
