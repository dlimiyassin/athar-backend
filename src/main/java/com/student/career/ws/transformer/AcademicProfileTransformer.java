package com.student.career.ws.transformer;

import com.student.career.bean.AcademicProfile;
import com.student.career.ws.dto.AcademicProfileDto;
import com.student.career.zBase.transformer.AbstractTransformer;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AcademicProfileTransformer
        extends AbstractTransformer<AcademicProfile, AcademicProfileDto> {

    private final DiplomeTransformer diplomeTransformer;

    public AcademicProfileTransformer(DiplomeTransformer diplomeTransformer) {
        this.diplomeTransformer = diplomeTransformer;
    }

    @Override
    public AcademicProfile toEntity(AcademicProfileDto dto) {
        if (dto == null) {
            return null;
        }

        AcademicProfile profile = new AcademicProfile();
        profile.setCurrentDiploma(diplomeTransformer.toEntity(dto.currentDiploma()));
        profile.setCustomAttributes(dto.customAttributes());

        profile.setDiplomes(
                dto.diplomes() != null
                        ? dto.diplomes()
                        .stream()
                        .map(diplomeTransformer::toEntity)
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
                diplomeTransformer.toDto(entity.getCurrentDiploma()),
                entity.getCustomAttributes(),
                entity.getDiplomes() != null
                        ? entity.getDiplomes()
                        .stream()
                        .map(diplomeTransformer::toDto)
                        .collect(Collectors.toList())
                        : null
        );
    }
}
