package com.devx.staff_service.service;

import com.devx.staff_service.dto.StaffDto;
import com.devx.staff_service.exception.StaffMemberNotFoundException;
import com.devx.staff_service.model.Staff;
import com.devx.staff_service.repository.StaffRepository;
import com.devx.staff_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Optional;

@Component
public class StaffServiceIntegration {

    private final Scheduler jdbcScheduler;
    private final StaffRepository staffRepository;

    @Autowired
    public StaffServiceIntegration(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, StaffRepository staffRepository) {
        this.jdbcScheduler = jdbcScheduler;
        this.staffRepository = staffRepository;
    }

    private Staff addStaffInternal(Staff staff) {
        return staffRepository.save(staff);
    }

    public Mono<StaffDto> addStaff(Staff staff) {
        return Mono.fromCallable(() -> addStaffInternal(staff)).subscribeOn(jdbcScheduler).map(AppUtils::convertStaffToStaffDto);
    }

    private Staff updateStaffInternal(Staff updatedStaff) {
        return staffRepository.updateOrInsert(updatedStaff);
    }

    public Mono<StaffDto> updateStaff(Staff updatedStaff) {
        return Mono.fromCallable(() -> updateStaffInternal(updatedStaff)).subscribeOn(jdbcScheduler).map(AppUtils::convertStaffToStaffDto);
    }

    private void deleteStaffInternal(Long id) {
        Optional<Staff> existingStaffOptional = staffRepository.findById(id);
        if(existingStaffOptional.isPresent())
        {
            staffRepository.deleteById(id);
        }
        else{
            throw new StaffMemberNotFoundException("Staff member not found");
        }
    }

    public Mono<Void> deleteStaff(Long id) {
        deleteStaffInternal(id);
        return Mono.empty();
    }

    private List<StaffDto> getAllStaffInternal()
    {
        return staffRepository.findAll().stream().map(AppUtils::convertStaffToStaffDto).toList();
    }

    public Flux<StaffDto> getAllStaff() {
        return Mono.fromCallable(this::getAllStaffInternal).subscribeOn(jdbcScheduler).flatMapMany(Flux::fromIterable);
    }
}
