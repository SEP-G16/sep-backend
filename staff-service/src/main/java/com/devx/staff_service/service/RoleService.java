package com.devx.staff_service.service;

import com.devx.staff_service.dto.RoleDto;
import com.devx.staff_service.exception.BadRequestException;
import com.devx.staff_service.exception.NullFieldException;
import com.devx.staff_service.model.Role;
import com.devx.staff_service.repository.RoleRepository;
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
public class RoleService {

    private final RoleServiceIntegration roleServiceIntegration;

    private static final Logger LOG = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    public RoleService(RoleServiceIntegration roleServiceIntegration) {
        this.roleServiceIntegration = roleServiceIntegration;
    }

    public Mono<RoleDto> addRole(RoleDto staffDto) {
        Role staff = AppUtils.RoleUtils.convertRoleDtoToRole(staffDto);
        return roleServiceIntegration.addRole(staff);
    }

    public Flux<RoleDto> getAllRole(){
        return roleServiceIntegration.getAllRole();
    }


    public Mono<Void> deleteRole(Long id) {
        return roleServiceIntegration.deleteRole(id);
    }
}
