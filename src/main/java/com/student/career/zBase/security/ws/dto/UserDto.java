package com.student.career.zBase.security.ws.dto;

import com.student.career.zBase.security.bean.enums.UserStatus;

import java.util.List;


public record UserDto(
        String id,
        String firstName,
        String lastName,
        String email,
        String password,
        String phoneNumber,
        boolean enabled,
        UserStatus status,
        String lastLogin,
        List<RoleDto> roleDtos
) {
}