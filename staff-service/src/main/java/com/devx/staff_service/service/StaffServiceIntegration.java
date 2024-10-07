package com.devx.staff_service.service;

import com.devx.staff_service.dto.StaffDto;
import com.devx.staff_service.exception.RoleNotFoundException;
import com.devx.staff_service.exception.StaffMemberNotFoundException;
import com.devx.staff_service.model.Role;
import com.devx.staff_service.model.Staff;
import com.devx.staff_service.repository.RoleRepository;
import com.devx.staff_service.repository.StaffRepository;
import com.devx.staff_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.management.relation.RoleInfoNotFoundException;
import java.util.List;
import java.util.Optional;

@Component
public class StaffServiceIntegration {

    private final Scheduler jdbcScheduler;
    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public StaffServiceIntegration(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, StaffRepository staffRepository, RoleRepository roleRepository) {
        this.jdbcScheduler = jdbcScheduler;
        this.staffRepository = staffRepository;
        this.roleRepository = roleRepository;
    }

    private Role findRole(Role role)
    {
        if(role.getId() != null)
        {
            return roleRepository.findById(role.getId()).orElseThrow(RoleNotFoundException::new);
        }
        else {
            return roleRepository.findByName(role.getName()).orElseThrow(RoleNotFoundException::new);
        }
    }

    private Staff addStaffInternal(Staff staff) {
        Role role = findRole(staff.getRole());
        staff.setRole(role);
        return staffRepository.save(staff);
    }

    public Mono<StaffDto> addStaff(Staff staff) {
        return Mono.fromCallable(() -> addStaffInternal(staff)).subscribeOn(jdbcScheduler).map(AppUtils.StaffUtils::convertStaffToStaffDto);
    }

    private Staff updateStaffInternal(Staff updatedStaff) {
        Role role = findRole(updatedStaff.getRole());
        updatedStaff.setRole(role);
        return staffRepository.updateOrInsert(updatedStaff);
    }

    public Mono<StaffDto> updateStaff(Staff updatedStaff) {
        return Mono.fromCallable(() -> {
            Staff updatedEntity = updateStaffInternal(updatedStaff);
            return AppUtils.StaffUtils.convertStaffToStaffDto(updatedEntity);
        }).subscribeOn(jdbcScheduler);
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

    private List<Staff> getAllStaffInternal()
    {
        return staffRepository.findAll();
    }

    public Flux<StaffDto> getAllStaff() {
        return Mono.fromCallable(() -> getAllStaffInternal().stream().map(AppUtils.StaffUtils::convertStaffToStaffDto).toList()).subscribeOn(jdbcScheduler).flatMapMany(Flux::fromIterable);
    }
}
