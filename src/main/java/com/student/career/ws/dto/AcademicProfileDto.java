package com.student.career.ws.dto;

import java.util.List;
import java.util.Map;

public record AcademicProfileDto(
        String ecole,
        String program,
        Integer year,
        Map<String, Object> customAttributes,
        List<DiplomeDto> diplomes
) {
}
