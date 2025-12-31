package com.student.career.ws.transformer;

import com.student.career.bean.AcademicProfile;
import com.student.career.ws.dto.AcademicProfileDto;
import com.student.career.zBase.transformer.AbstractTransformer;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AcademicProfileTransformer
        extends AbstractTransformer<AcademicProfile, AcademicProfileDto> {

    private final DiplomaTransformer diplomaTransformer;

    public AcademicProfileTransformer(DiplomaTransformer diplomaTransformer) {
        this.diplomaTransformer = diplomaTransformer;
    }

    @Override
    public AcademicProfile toEntity(AcademicProfileDto dto) {
        if (dto == null) {
            return null;
        }

        AcademicProfile profile = new AcademicProfile();
        profile.setGender(dto.gender());
        profile.setCurrentDiploma(diplomaTransformer.toEntity(dto.currentDiploma()));
        profile.setCustomAttributes(dto.customAttributes());

        profile.setDiplomas(
                dto.diplomas() != null
                        ? dto.diplomas()
                        .stream()
                        .map(diplomaTransformer::toEntity)
                        .collect(Collectors.toList())
                        : null
        );

        return profile;
    }

    @Override
    public AcademicProfileDto toDto(AcademicProfile entity) {
        if (entity == null) {
            return null;
        }

        return new AcademicProfileDto(
                entity.getGender(),
                diplomaTransformer.toDto(entity.getCurrentDiploma()),
                entity.getCustomAttributes(),
                entity.getDiplomas() != null
                        ? entity.getDiplomas()
                        .stream()
                        .map(diplomaTransformer::toDto)
                        .collect(Collectors.toList())
                        : null
        );
    }
}
