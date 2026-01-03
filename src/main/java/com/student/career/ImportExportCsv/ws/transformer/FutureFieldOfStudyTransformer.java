package com.student.career.ImportExportCsv.ws.transformer;

import com.student.career.ImportExportCsv.beans.FutureFieldOfStudy;
import com.student.career.ImportExportCsv.ws.dto.FutureFieldOfStudyDto;
import com.student.career.zBase.transformer.AbstractTransformer;
import org.springframework.stereotype.Component;

@Component
public class FutureFieldOfStudyTransformer
        extends AbstractTransformer<FutureFieldOfStudy, FutureFieldOfStudyDto> {

    @Override
    public FutureFieldOfStudy toEntity(FutureFieldOfStudyDto dto) {
        if (dto == null) return null;

        FutureFieldOfStudy entity = new FutureFieldOfStudy();
        entity.setId(dto.id());
        entity.setCode(dto.code());
        entity.setLabel(dto.label());
        entity.setActive(dto.active());

        return entity;
    }

    @Override
    public FutureFieldOfStudyDto toDto(FutureFieldOfStudy entity) {
        if (entity == null) return null;

        return new FutureFieldOfStudyDto(
                entity.getId(),
                entity.getCode(),
                entity.getLabel(),
                entity.isActive()
        );
    }
}

