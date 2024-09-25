package com.devx.staff_service.repository;

import com.devx.staff_service.model.Staff;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Transactional
    default Staff updateOrInsert(Staff staff) {
        return save(staff);
    }
}