package com.devx.staff_service.service;

import com.devx.staff_service.model.Staff;
import com.devx.staff_service.repository.StaffRepository;
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
public class StaffService {

    private final StaffRepository staffRepository;
    private final Scheduler jdbcScheduler;

    private static final Logger LOG = LoggerFactory.getLogger(StaffService.class);

    @Autowired
    public StaffService(StaffRepository staffRepository, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler) {
        this.staffRepository = staffRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    private Staff insertStaffMember(Staff staff) {
        return staffRepository.save(staff);
    }

    public ResponseEntity<Mono<Staff>> addStaff(Staff staff) {
        try {
            Mono<Staff> mono = Mono.fromCallable(() -> insertStaffMember(staff)).subscribeOn(jdbcScheduler);
            return new ResponseEntity<>(mono, HttpStatus.CREATED);
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while adding staff", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Mono<Staff>> updateStaff(Long id, Staff updatedStaff) {
        try {
            Mono<Staff> mono = Mono.fromCallable(() -> {
                return staffRepository.updateOrInsert(updatedStaff);
            }).subscribeOn(jdbcScheduler);

            return new ResponseEntity<>(mono, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while updating staff", e);
            return new ResponseEntity<>(Mono.error(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Mono<Void>> deleteStaff(Long id) {
        try {
            Mono<Void> deleteMono = Mono.fromRunnable(() -> staffRepository.deleteById(id))
                    .subscribeOn(jdbcScheduler).then();

            return new ResponseEntity<>(deleteMono, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while deleting staff", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
