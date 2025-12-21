package com.student.career.bean;

import com.student.career.bean.enums.NiveauEtude;
import com.student.career.bean.enums.Universite;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Diplome {
    private Universite universite;
    private String ecole;
    private NiveauEtude niveauEtude;
    private String filiere;
    private String intitule;
    private Integer year;
    private Double note;
}
