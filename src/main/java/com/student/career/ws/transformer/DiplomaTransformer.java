package com.student.career.ws.transformer;

import com.student.career.bean.Diploma;
import com.student.career.ws.dto.DiplomaDto;
import com.student.career.zBase.transformer.AbstractTransformer;
import org.springframework.stereotype.Component;

@Component
public class DiplomaTransformer
        extends AbstractTransformer<Diploma, DiplomaDto> {

    @Override
    public Diploma toEntity(DiplomaDto dto) {
        if (dto == null) {
            return null;
        }

        Diploma d = new Diploma();
        d.setUniversity(dto.university());
        d.setSchool(dto.school());
        d.setStudyLevel(dto.studyLevel());
        d.setStudyField(dto.studyField());
        d.setTitle(dto.title());
        d.setYear(dto.year());
        d.setGrade(dto.grade());
        return d;
    }

    @Override
    public DiplomaDto toDto(Diploma entity) {
        if (entity == null) {
            return null;
        }

        return new DiplomaDto(
                entity.getUniversity(),
                entity.getSchool(),
                entity.getStudyLevel(),
                entity.getStudyField(),
                entity.getTitle(),
                entity.getYear(),
                entity.getGrade()
        );
    }
}
