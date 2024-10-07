package com.devx.staff_service.controller;

import com.devx.staff_service.dto.StaffDto;
import com.devx.staff_service.exception.BadRequestException;
import com.devx.staff_service.exception.NullFieldException;
import com.devx.staff_service.exception.RoleNotFoundException;
import com.devx.staff_service.exception.StaffMemberNotFoundException;
import com.devx.staff_service.model.Staff;
import com.devx.staff_service.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;

    public static Logger LOG = LoggerFactory.getLogger(StaffController.class);

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<StaffDto>> addStaff(@RequestBody StaffDto staffDto) {
        try{
            if(staffDto.hasNullFields())
            {
                throw new NullFieldException("Staff fields cannot be null");
            }
            if(staffDto.invalidFrom())
            {
                throw new BadRequestException("Invalid form data");
            }
            return ResponseEntity.created(null).body(staffService.addStaff(staffDto));
        }catch (BadRequestException | NullFieldException | RoleNotFoundException e){
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e){
            LOG.error("Error occurred while adding staff: ", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<StaffDto>> getAllStaff() {
        try{
            return ResponseEntity.ok().body(staffService.getAllStaff());
        } catch (Exception e){
            LOG.error("Error occurred while fetching all staff: ", e);
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Mono<StaffDto>> updateStaff(@RequestBody StaffDto updatedStaffDto) {
        try{
            if(updatedStaffDto.hasNullFields())
            {
                throw new NullFieldException("Staff fields cannot be null");
            }
            if(updatedStaffDto.invalidFrom())
            {
                throw new BadRequestException("Invalid form data");
            }
            return ResponseEntity.ok().body(staffService.updateStaff(updatedStaffDto));
        }catch (BadRequestException | NullFieldException | RoleNotFoundException e){
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e){
            LOG.error("Error occurred while updating staff: ", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Mono<Void>> removeStaff(@PathVariable Long id) {
        try{
            if(id == null)
            {
                throw new BadRequestException("Staff id cannot be null");
            }
            return ResponseEntity.ok().body(staffService.deleteStaff(id));
        }catch (StaffMemberNotFoundException | BadRequestException e){
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e){
            LOG.error("Error occurred while deleting staff: ", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }
}
