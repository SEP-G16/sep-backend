package com.devx.staff_service.service;

import com.devx.staff_service.dto.RoleDto;
import com.devx.staff_service.exception.RoleNotFoundException;
import com.devx.staff_service.model.Role;
import com.devx.staff_service.repository.RoleRepository;
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
public class RoleServiceIntegration {

    private final Scheduler jdbcScheduler;
    private final RoleRepository roleRepository;


    @Autowired
    public RoleServiceIntegration(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, RoleRepository roleRepository) {
        this.jdbcScheduler = jdbcScheduler;
        this.roleRepository = roleRepository;
    }

    private Role addRoleInternal(Role staff) {
        return roleRepository.save(staff);
    }

    public Mono<RoleDto> addRole(Role staff) {
        return Mono.fromCallable(() -> addRoleInternal(staff)).subscribeOn(jdbcScheduler).map(AppUtils.RoleUtils::convertRoleToRoleDto);
    }

    private void deleteRoleInternal(Long id) {
        Optional<Role> existingRoleOptional = roleRepository.findById(id);
        if(existingRoleOptional.isPresent())
        {
            roleRepository.deleteById(id);
        }
        else{
            throw new RoleNotFoundException("Role member not found");
        }
    }

    public Mono<Void> deleteRole(Long id) {
        deleteRoleInternal(id);
        return Mono.empty();
    }

    private List<Role> getAllRoleInternal()
    {
        return roleRepository.findAll();
    }

    public Flux<RoleDto> getAllRole() {
        return Mono.fromCallable(() -> getAllRoleInternal().stream().map(AppUtils.RoleUtils::convertRoleToRoleDto).toList()).subscribeOn(jdbcScheduler).flatMapMany(Flux::fromIterable);
    }
}
