package com.devx.staff_service.utils;

import com.devx.staff_service.dto.RoleDto;
import com.devx.staff_service.dto.StaffDto;
import com.devx.staff_service.exception.NullFieldException;
import com.devx.staff_service.model.Role;
import com.devx.staff_service.model.Staff;

public class AppUtils {
    private AppUtils() {
    }

    public static class StaffUtils{
        public static Staff convertStaffDtoToStaff(StaffDto staffDto) {
            Staff staff = new Staff();
            staff.setId(staffDto.getId());
            staff.setName(staffDto.getName());
            staff.setRole(RoleUtils.convertRoleDtoToRole(staffDto.getRole()));
            staff.setGender(staffDto.getGender());
            staff.setDateOfBirth(staffDto.getDateOfBirth());
            staff.setAddress(staffDto.getAddress());
            staff.setPhoneNumber(staffDto.getPhoneNumber());
            staff.setEmail(staffDto.getEmail());
            staff.setCreatedAt(staffDto.getCreatedAt());
            staff.setUpdatedAt(staffDto.getUpdatedAt());
            return staff;
        }

        public static StaffDto convertStaffToStaffDto(Staff staff)
        {

            StaffDto staffDto = new StaffDto();
            staffDto.setId(staff.getId());
            staffDto.setName(staff.getName());
            staffDto.setRole(RoleUtils.convertRoleToRoleDto(staff.getRole()));
            staffDto.setGender(staff.getGender());
            staffDto.setDateOfBirth(staff.getDateOfBirth());
            staffDto.setAddress(staff.getAddress());
            staffDto.setPhoneNumber(staff.getPhoneNumber());
            staffDto.setEmail(staff.getEmail());
            staffDto.setCreatedAt(staff.getCreatedAt());
            staffDto.setUpdatedAt(staff.getUpdatedAt());
            return staffDto;
        }
    }

    public static class RoleUtils{
        public static Role convertRoleDtoToRole(RoleDto roleDto) {
            Role role = new Role();
            role.setId(roleDto.getId());
            role.setName(roleDto.getName());
            role.setCreatedAt(roleDto.getCreatedAt());
            role.setUpdatedAt(roleDto.getUpdatedAt());
            return role;
        }

        public static RoleDto convertRoleToRoleDto(Role role)
        {
            RoleDto roleDto = new RoleDto();
            roleDto.setId(role.getId());
            roleDto.setName(role.getName());
            roleDto.setCreatedAt(role.getCreatedAt());
            roleDto.setUpdatedAt(role.getUpdatedAt());
            return roleDto;
        }
    }
}
