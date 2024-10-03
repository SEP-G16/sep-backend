package com.devx.staff_service;

import com.devx.staff_service.model.Staff;
import com.devx.staff_service.repository.StaffRepository;
import com.devx.staff_service.service.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Objects;

public class StaffServiceIntegrationTests extends BaseIntegrationTestConfiguration {

    private final StaffRepository staffRepository;
    private final StaffService staffService;

    @Autowired
    public StaffServiceIntegrationTests(StaffRepository staffRepository, StaffService staffService) {
        this.staffRepository = staffRepository;
        this.staffService = staffService;
    }

    private Staff testStaff1;
    private Staff testStaff2;

    @BeforeEach
    void setUp() {
        // Initialize test data for staff
        testStaff1 = new Staff();
        testStaff1.setName("John Doe");
        testStaff1.setRole("Manager");
        testStaff1.setEmail("johndoe@example.com");
        testStaff1.setPhoneNumber("1234567890");
        testStaff1.setGender("Male");
        testStaff1.setDateOfBirth(LocalDate.of(1985, 5, 15));
        testStaff1.setAddress("123 Main St, City, Country");

        testStaff2 = new Staff();
        testStaff2.setName("Jane Smith");
        testStaff2.setRole("Waitress");
        testStaff2.setEmail("janesmith@example.com");
        testStaff2.setPhoneNumber("0987654321");
        testStaff2.setGender("Female");
        testStaff2.setDateOfBirth(LocalDate.of(1990, 7, 20));
        testStaff2.setAddress("456 Another St, City, Country");
    }

    @Test
    void testAddStaff() {
        Staff savedStaff1 = staffRepository.save(testStaff1);
        Staff savedStaff2 = staffRepository.save(testStaff2);

        assert savedStaff1.getId() != null;
        assert savedStaff2.getId() != null;

        assert savedStaff1.getName().equals(testStaff1.getName());
        assert savedStaff1.getRole().equals(testStaff1.getRole());
        assert savedStaff1.getEmail().equals(testStaff1.getEmail());
        assert savedStaff1.getPhoneNumber().equals(testStaff1.getPhoneNumber());
        assert savedStaff1.getGender().equals(testStaff1.getGender());
        assert savedStaff1.getDateOfBirth().equals(testStaff1.getDateOfBirth());
        assert savedStaff1.getAddress().equals(testStaff1.getAddress());

        assert savedStaff2.getName().equals(testStaff2.getName());
        assert savedStaff2.getRole().equals(testStaff2.getRole());
        assert savedStaff2.getEmail().equals(testStaff2.getEmail());
        assert savedStaff2.getPhoneNumber().equals(testStaff2.getPhoneNumber());
        assert savedStaff2.getGender().equals(testStaff2.getGender());
        assert savedStaff2.getDateOfBirth().equals(testStaff2.getDateOfBirth());
        assert savedStaff2.getAddress().equals(testStaff2.getAddress());
    }

    @Test
    void testUpdateStaff() {
        Staff savedStaff = staffRepository.save(testStaff1);

        // Update details
        savedStaff.setName("John Updated");
        savedStaff.setPhoneNumber("9876543210");

        Staff updatedStaff = staffRepository.save(savedStaff);

        assert updatedStaff != null;
        assert updatedStaff.getId().equals(savedStaff.getId());
        assert updatedStaff.getName().equals("John Updated");
        assert updatedStaff.getPhoneNumber().equals("9876543210");
    }

    @Test
    void testRemoveStaff() {
        Staff savedStaff = staffRepository.save(testStaff1);

        staffRepository.deleteById(savedStaff.getId());

        Staff deletedStaff = staffRepository.findById(savedStaff.getId()).orElse(null);
        assert deletedStaff == null;
    }

    @Test
    void testDeleteStaffSuccess() {
        // Assuming staff with ID 1 exists
        Long validStaffId = 1L;

        Mono<Void> response = staffService.deleteStaff(validStaffId);

    }

}
