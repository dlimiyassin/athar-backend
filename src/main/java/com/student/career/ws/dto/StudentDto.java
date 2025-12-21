package com.student.career.ws.dto;

import com.student.career.zBase.security.ws.dto.UserDto;

public record StudentDto(
        String id,
        UserDto user,
        AcademicProfileDto academicProfile
) {
}
