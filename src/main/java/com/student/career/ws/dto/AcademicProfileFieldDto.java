package com.student.career.ws.dto;


import com.student.career.bean.enums.FieldType;

public record AcademicProfileFieldDto(
        String id,
        String name,
        String label,
        FieldType type,
        boolean required
) {
}
