package com.student.career.ImportExportCsv.ws.dto;

public record PredictionResultDto(
        String id,
        String studentId,
        PredictionTypeDto predictionTypeDto,
        String rawValue,
        String interpretedValue,
        String generatedAt,
        String modelVersion
) {
}

