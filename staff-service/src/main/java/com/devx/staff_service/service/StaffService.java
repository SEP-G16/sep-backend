package com.devx.staff_service.service;

import com.devx.staff_service.dto.StaffDto;
import com.devx.staff_service.exception.BadRequestException;
import com.devx.staff_service.exception.NullFieldException;
import com.devx.staff_service.model.Staff;
import com.devx.staff_service.repository.StaffRepository;
import com.devx.staff_service.utils.AppUtils;
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

    private final StaffServiceIntegration staffServiceIntegration;

    private static final Logger LOG = LoggerFactory.getLogger(StaffService.class);

    @Autowired
    public StaffService(StaffServiceIntegration staffServiceIntegration) {
        this.staffServiceIntegration = staffServiceIntegration;
    }

    public Mono<StaffDto> addStaff(StaffDto staffDto) {
        Staff staff = AppUtils.StaffUtils.convertStaffDtoToStaff(staffDto);
        return staffServiceIntegration.addStaff(staff);
    }

    public Flux<StaffDto> getAllStaff(){
        return staffServiceIntegration.getAllStaff();
    }

    public Mono<StaffDto> updateStaff(StaffDto updatedStaffDto) {
        Staff updatedStaff = AppUtils.StaffUtils.convertStaffDtoToStaff(updatedStaffDto);
        if(updatedStaff.getId() == null) {
            throw new NullFieldException("Staff ID is required for updating staff");
        }
        return staffServiceIntegration.updateStaff(updatedStaff);
    }

    public Mono<Void> deleteStaff(Long id) {
        return staffServiceIntegration.deleteStaff(id);
    }
}
