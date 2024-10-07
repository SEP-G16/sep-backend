package com.devx.staff_service.controller;

import com.devx.staff_service.dto.RoleDto;
import com.devx.staff_service.exception.BadRequestException;
import com.devx.staff_service.exception.NullFieldException;
import com.devx.staff_service.exception.RoleNotFoundException;
import com.devx.staff_service.model.Role;
import com.devx.staff_service.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/staff/role")
public class RoleController {

    private final RoleService roleService;

    public static Logger LOG = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<RoleDto>> addRole(@RequestBody RoleDto roleDto) {
        try{
            if(roleDto.isEveryFiledNull())
            {
                throw new NullFieldException("Role fields cannot be null");
            }
            if(roleDto.invalidForm())
            {
                throw new BadRequestException("Invalid form data");
            }
            return ResponseEntity.created(null).body(roleService.addRole(roleDto));
        }catch (BadRequestException | NullFieldException e){
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e){
            LOG.error("Error occurred while adding role: ", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<RoleDto>> getAllRole() {
        try{
            return ResponseEntity.ok().body(roleService.getAllRole());
        } catch (Exception e){
            LOG.error("Error occurred while fetching all role: ", e);
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Mono<Void>> removeRole(@PathVariable Long id) {
        try{
            if(id == null)
            {
                throw new BadRequestException("Role id cannot be null");
            }
            return ResponseEntity.ok().body(roleService.deleteRole(id));
        }catch (RoleNotFoundException | BadRequestException e){
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e){
            LOG.error("Error occurred while deleting role: ", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }
}
