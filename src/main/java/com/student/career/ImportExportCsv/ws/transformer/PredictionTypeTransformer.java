package com.student.career.ImportExportCsv.ws.transformer;

import com.student.career.ImportExportCsv.beans.PredictionType;
import com.student.career.ImportExportCsv.ws.dto.PredictionTypeDto;
import com.student.career.zBase.transformer.AbstractTransformer;
import org.springframework.stereotype.Component;

@Component
public class PredictionTypeTransformer
        extends AbstractTransformer<PredictionType, PredictionTypeDto> {

    @Override
    public PredictionType toEntity(PredictionTypeDto dto) {
        if (dto == null) return null;

        PredictionType entity = new PredictionType();
        entity.setId(dto.id());
        entity.setCode(dto.code());
        entity.setLabel(dto.label());
        entity.setValueType(dto.valueType());
        entity.setDescription(dto.description());
        entity.setActive(dto.active());

        return entity;
    }

    @Override
    public PredictionTypeDto toDto(PredictionType entity) {
        if (entity == null) return null;

        return new PredictionTypeDto(
                entity.getId(),
                entity.getCode(),
                entity.getLabel(),
                entity.getValueType(),
                entity.getDescription(),
                entity.isActive()
        );
    }
}
