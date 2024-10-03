package com.devx.staff_service.utils;

import com.devx.staff_service.dto.StaffDto;
import com.devx.staff_service.exception.NullFieldException;
import com.devx.staff_service.model.Staff;

public class AppUtils {
    private AppUtils() {
    }

    public static Staff convertStaffDtoToStaff(StaffDto staffDto) {
        if(staffDto.hasNullFields())
        {
            throw new NullFieldException("StaffDto has null fields");
        }
        Staff staff = new Staff();
        staff.setId(staffDto.getId());
        staff.setName(staffDto.getName());
        staff.setRole(staffDto.getRole());
        staff.setGender(staffDto.getGender());
        staff.setDateOfBirth(staffDto.getDateOfBirth());
        staff.setAddress(staffDto.getAddress());
        staff.setPhoneNumber(staffDto.getPhoneNumber());
        staff.setEmail(staffDto.getEmail());
        return staff;
    }

    public static StaffDto convertStaffToStaffDto(Staff staff)
    {

        StaffDto staffDto = new StaffDto();
        staffDto.setId(staff.getId());
        staffDto.setName(staff.getName());
        staffDto.setRole(staff.getRole());
        staffDto.setGender(staff.getGender());
        staffDto.setDateOfBirth(staff.getDateOfBirth());
        staffDto.setAddress(staff.getAddress());
        staffDto.setPhoneNumber(staff.getPhoneNumber());
        staffDto.setEmail(staff.getEmail());
        return staffDto;
    }
}
