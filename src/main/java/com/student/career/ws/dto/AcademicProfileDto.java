package com.student.career.ws.dto;

import java.util.List;
import java.util.Map;

public record AcademicProfileDto(
        DiplomaDto currentDiploma,
        Map<String, Object> customAttributes,
        List<DiplomaDto> diplomas
) {
}
