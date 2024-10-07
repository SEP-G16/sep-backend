package com.devx.staff_service;

import com.devx.staff_service.dto.RoleDto;
import com.devx.staff_service.dto.StaffDto;
import com.devx.staff_service.enums.Gender;
import com.devx.staff_service.model.Role;
import com.devx.staff_service.model.Staff;
import com.devx.staff_service.repository.RoleRepository;
import com.devx.staff_service.repository.StaffRepository;
import com.devx.staff_service.service.StaffService;
import com.devx.staff_service.utils.AppUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Objects;

public class StaffServiceIntegrationTests extends BaseIntegrationTestConfiguration {

    private final StaffRepository staffRepository;
    private final StaffService staffService;
    private final RoleRepository roleRepository;

    @Autowired
    public StaffServiceIntegrationTests(StaffRepository staffRepository, StaffService staffService, RoleRepository roleRepository) {
        this.staffRepository = staffRepository;
        this.staffService = staffService;
        this.roleRepository = roleRepository;
    }

    private StaffDto testStaffDto1;
    private StaffDto testStaffDto2;

    private RoleDto roleDto1;
    private RoleDto roleDto2;

    @BeforeEach
    void setUp() {
        staffRepository.deleteAll();
        roleRepository.deleteAll();

        Role role1 = new Role();
        role1.setName("Manager");

        Role role2 = new Role();
        role2.setName("Waitress");

        roleRepository.save(role1);
        roleRepository.save(role2);

        roleDto1 = AppUtils.RoleUtils.convertRoleToRoleDto(role1);
        roleDto2 = AppUtils.RoleUtils.convertRoleToRoleDto(role2);

        testStaffDto1 = new StaffDto();
        testStaffDto1.setName("John Doe");
        testStaffDto1.setRole(roleDto1);
        testStaffDto1.setEmail("johndoe@example.com");
        testStaffDto1.setPhoneNumber("1234567890");
        testStaffDto1.setGender(Gender.Male);
        testStaffDto1.setDateOfBirth(LocalDate.of(1985, 5, 15));
        testStaffDto1.setAddress("123 Main St, City, Country");

        testStaffDto2 = new StaffDto();
        testStaffDto2.setName("Jane Smith");
        testStaffDto2.setRole(roleDto2);
        testStaffDto2.setEmail("janesmith@example.com");
        testStaffDto2.setPhoneNumber("0987654321");
        testStaffDto2.setGender(Gender.Female);
        testStaffDto2.setDateOfBirth(LocalDate.of(1990, 7, 20));
        testStaffDto2.setAddress("456 Another St, City, Country");
    }

    @Test
    void testAddStaff() {
        Mono<StaffDto> savedStaffPublisher = staffService.addStaff(testStaffDto1);
        StepVerifier.create(savedStaffPublisher)
                .assertNext(savedStaff -> {
                    assert savedStaff.getId() != null;
                    assert savedStaff.getName().equals(testStaffDto1.getName());
                    assert savedStaff.getEmail().equals(testStaffDto1.getEmail());
                    assert savedStaff.getPhoneNumber().equals(testStaffDto1.getPhoneNumber());
                    assert savedStaff.getGender().equals(testStaffDto1.getGender());
                    assert savedStaff.getDateOfBirth().equals(testStaffDto1.getDateOfBirth());
                    assert savedStaff.getAddress().equals(testStaffDto1.getAddress());
                })
                .verifyComplete();
    }

    @Test
    void testAddStaffWithSameRole()
    {
        StaffDto updatedTestStaffDto2 = testStaffDto2;
        updatedTestStaffDto2.setRole(roleDto1);

        Mono<StaffDto> savedStaffPublisher1 = staffService.addStaff(testStaffDto1);
        savedStaffPublisher1.block();

        Mono<StaffDto> savedStaffPublisher2 = staffService.addStaff(updatedTestStaffDto2);
        StepVerifier.create(savedStaffPublisher2)
                .assertNext(savedStaff -> {
                    assert savedStaff.getId() != null;
                    assert savedStaff.getRole().getId().equals(roleDto1.getId());
                })
                .verifyComplete();
    }

    @Test
    void testUpdateStaff() {
        Mono<StaffDto> savedStaffPublisher = staffService.addStaff(testStaffDto2);
        StaffDto savedStaff = savedStaffPublisher.block();

        assert savedStaff != null;
        savedStaff.setName("Jane Doe");

        Mono<StaffDto> updatedStaffPublisher = staffService.updateStaff(savedStaff);
        StepVerifier.create(updatedStaffPublisher)
                .assertNext(updatedStaff -> {
                    assert updatedStaff.getId().equals(savedStaff.getId());
                    assert updatedStaff.getName().equals("Jane Doe");
                    assert updatedStaff.getEmail().equals(savedStaff.getEmail());
                    assert updatedStaff.getPhoneNumber().equals(savedStaff.getPhoneNumber());
                    assert updatedStaff.getGender().equals(savedStaff.getGender());
                    assert updatedStaff.getDateOfBirth().equals(savedStaff.getDateOfBirth());
                    assert updatedStaff.getAddress().equals(savedStaff.getAddress());
                })
                .verifyComplete();
    }

    @Test
    void testRemoveStaff() {

        Mono<StaffDto> savedStaff1Publisher = staffService.addStaff(testStaffDto1);
        StaffDto saved1 = savedStaff1Publisher.block();

        Mono<StaffDto> savedStaff2Publisher = staffService.addStaff(testStaffDto2);
        savedStaff2Publisher.block();

        Flux<StaffDto> allStaffPublisher = staffService.getAllStaff();
        StepVerifier.create(allStaffPublisher)
                .expectNextCount(2)
                .verifyComplete();

        assert saved1 != null;
        Mono<Void> deleteStaffPublisher = staffService.deleteStaff(saved1.getId());
        deleteStaffPublisher.block();

        Flux<StaffDto> afterDeleteAllStaffPublisher = staffService.getAllStaff();
        StepVerifier.create(afterDeleteAllStaffPublisher)
                .expectNextCount(1)
                .verifyComplete();
    }
}
