package com.student.career.ws.dto;

import com.student.career.bean.enums.TargetType;

import java.util.List;

public record SurveyDto(
        String id,
        String title,
        String description,
        String createdByTeacherId,
        List<QuestionDto> questions,
        TargetType target,
        String createdAt
) {
}
