package com.student.career.ws.transformer;

import com.student.career.bean.Diplome;
import com.student.career.ws.dto.DiplomeDto;
import com.student.career.zBase.transformer.AbstractTransformer;
import org.springframework.stereotype.Component;

@Component
public class DiplomeTransformer
        extends AbstractTransformer<Diplome, DiplomeDto> {

    @Override
    public Diplome toEntity(DiplomeDto dto) {
        if (dto == null) {
            return null;
        }

        Diplome d = new Diplome();
        d.setUniversite(dto.universite());
        d.setEcole(dto.ecole());
        d.setNiveauEtude(dto.niveauEtude());
        d.setFiliere(dto.filiere());
        d.setIntitule(dto.intitule());
        d.setYear(dto.year());
        d.setNote(dto.note());
        return d;
    }

    @Override
    public DiplomeDto toDto(Diplome entity) {
        if (entity == null) {
            return null;
        }

        return new DiplomeDto(
                entity.getUniversite(),
                entity.getEcole(),
                entity.getNiveauEtude(),
                entity.getFiliere(),
                entity.getIntitule(),
                entity.getYear(),
                entity.getNote()
        );
    }
}
