package com.student.career.ws.dto;

import java.util.List;

public record SurveyResponseDto(
        String id,
        String surveyLabel,
        String surveyId,
        String studentId,
        List<AnswerDto> answers,
        String submittedAt
) {
}
