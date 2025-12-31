package com.student.career.ws.dto;

import com.student.career.bean.enums.Gender;

import java.util.List;
import java.util.Map;

public record AcademicProfileDto(
        String gender,
        DiplomaDto currentDiploma,
        Map<String, Object> customAttributes,
        List<DiplomaDto> diplomas
) {
}
