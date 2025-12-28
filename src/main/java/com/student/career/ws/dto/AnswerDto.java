package com.student.career.ws.dto;

public record AnswerDto(
        String questionId,
        String questionLabel,
        String value
) {
}
