package com.student.career.ws.transformer;

import com.student.career.bean.AcademicProfileField;
import com.student.career.ws.dto.AcademicProfileFieldDto;
import com.student.career.zBase.transformer.AbstractTransformer;
import org.springframework.stereotype.Component;

@Component
public class AcademicProfileFieldTransformer
        extends AbstractTransformer<AcademicProfileField, AcademicProfileFieldDto> {

    @Override
    public AcademicProfileField toEntity(AcademicProfileFieldDto dto) {
        if (dto == null) {
            return null;
        }
        AcademicProfileField field = new AcademicProfileField();
        field.setId(dto.id());
        field.setName(dto.name());
        field.setLabel(dto.label());
        field.setType(dto.type());
        field.setRequired(dto.required());
        return field;
    }

    @Override
    public AcademicProfileFieldDto toDto(AcademicProfileField entity) {
        if (entity == null) {
            return null;
        }
        return new AcademicProfileFieldDto(
                entity.getId(),
                entity.getName(),
                entity.getLabel(),
                entity.getType(),
                entity.isRequired()
        );
    }
}
