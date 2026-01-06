package com.student.career.ImportExportCsv.ws.transformer;

import com.student.career.ImportExportCsv.beans.PredictionResult;
import com.student.career.ImportExportCsv.beans.PredictionType;
import com.student.career.ImportExportCsv.ws.dto.PredictionResultDto;
import com.student.career.zBase.transformer.AbstractTransformer;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PredictionResultTransformer
        extends AbstractTransformer<PredictionResult, PredictionResultDto> {

    private final PredictionTypeTransformer predictionTypeTransformer;

    public PredictionResultTransformer(PredictionTypeTransformer predictionTypeTransformer) {
        this.predictionTypeTransformer = predictionTypeTransformer;
    }

    @Override
    public PredictionResult toEntity(PredictionResultDto dto) {
        if (dto == null) {
            return null;
        }

        PredictionResult result = new PredictionResult();
        result.setId(dto.id());
        result.setStudentId(dto.studentId());
        result.setPredictionTypeId(dto.predictionTypeDto().id());
        result.setRawValue(dto.rawValue());
        result.setInterpretedValue(dto.interpretedValue());

        // convert generatedAt (String → Instant)
        if (dto.generatedAt() != null) {
            result.setGeneratedAt(Instant.parse(dto.generatedAt()));
        }

        result.setModelVersion(dto.modelVersion());

        return result;
    }

    @Override
    public PredictionResultDto toDto(PredictionResult entity) {
        if (entity == null) {
            return null;
        }

        return new PredictionResultDto(
                entity.getId(),
                entity.getStudentId(),
                null,
                entity.getRawValue(),
                entity.getInterpretedValue(),
                entity.getGeneratedAt() != null
                        ? entity.getGeneratedAt().toString()
                        : null,
                entity.getModelVersion()
        );
    }

    public PredictionResultDto toDto(PredictionResult predictionResult, PredictionType predictionType) {
        if (predictionResult == null) {
            return null;
        }

        return new PredictionResultDto(
                predictionResult.getId(),
                predictionResult.getStudentId(),
                predictionTypeTransformer.toDto(predictionType),
                predictionResult.getRawValue(),
                predictionResult.getInterpretedValue(),
                predictionResult.getGeneratedAt() != null
                        ? predictionResult.getGeneratedAt().toString()
                        : null,
                predictionResult.getModelVersion()
        );
    }
}
