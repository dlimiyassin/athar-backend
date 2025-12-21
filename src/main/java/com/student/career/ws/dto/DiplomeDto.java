package com.student.career.ws.dto;

import com.student.career.bean.enums.NiveauEtude;
import com.student.career.bean.enums.Universite;


public record DiplomeDto(
        Universite universite,
        String ecole,
        NiveauEtude niveauEtude,
        String filiere,
        String intitule,
        Integer year,
        Double note
) {
}
