package com.student.career.ImportExportCsv.ws.dto;

import com.student.career.ImportExportCsv.beans.PredictionValueType;

public record PredictionTypeDto(
        String id,
        String code,
        String label,
        PredictionValueType valueType,
        String description,
        boolean active
) {}

