package com.student.career.ws.dto;


import com.student.career.bean.enums.QuestionType;

import java.util.List;

public record QuestionDto(
        String id,
        QuestionType type,
        String label,
        List<String> options
) {
}
