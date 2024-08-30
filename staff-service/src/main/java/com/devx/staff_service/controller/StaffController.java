package com.devx.staff_service.controller;

import com.devx.staff_service.model.Staff;
import com.devx.staff_service.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<Staff>> addStaff(@RequestBody Staff staff) {
        return staffService.addStaff(staff);
    }

//    @GetMapping("/view/{id}")
//    public ResponseEntity<Mono<Staff>> getStaffById(@PathVariable Long id) {
//        return staffService.getStaffById(id);
//    }
//
//    @GetMapping("/view/name/{name}")
//    public ResponseEntity<Flux<Staff>> getStaffByName(@PathVariable String name) {
//        return staffService.getStaffByName(name);
//    }
//
//    @GetMapping("/view/position/{position}")
//    public ResponseEntity<Flux<Staff>> getStaffByPosition(@PathVariable String position) {
//        return staffService.getStaffByPosition(position);
//    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Mono<Staff>> updateStaff(@PathVariable Long id, @RequestBody Staff updatedStaff) {
        return staffService.updateStaff(id, updatedStaff);
    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Mono<Void>> removeStaff(@PathVariable Long id) {
        return staffService.deleteStaff(id);
    }
}
