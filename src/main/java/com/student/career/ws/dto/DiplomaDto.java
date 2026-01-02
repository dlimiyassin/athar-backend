package com.student.career.ws.dto;

import com.student.career.bean.enums.FieldOfStudy;
import com.student.career.bean.enums.StudyLevel;
import com.student.career.bean.enums.University;


public record DiplomaDto(
        University university,
        String school,
        StudyLevel studyLevel,
        FieldOfStudy studyField,
        String title,
        Integer year,
        Double grade
) {
}
