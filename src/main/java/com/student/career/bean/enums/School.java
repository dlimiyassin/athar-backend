package com.student.career.bean.enums;
import lombok.Getter;
@Getter
public enum School {

    FST("Faculté des Sciences et Techniques"),
    FS("Faculté des Sciences"),
    FG("Faculté des Sciences Juridiques, Economiques et Sociales"),
    FSE("Faculté des Sciences Economiques"),
    FSEG("Faculté des Sciences Economiques et de Gestion"),
    FMP("Faculté de Médecine et de Pharmacie"),
    ENF("Ecole Normale Supérieure"),
    ENSA("Ecole Nationale des Sciences Appliquées"),
    ENCG("Ecole Nationale de Commerce et de Gestion"),
    ENSET("Ecole Normale Supérieure de l’Enseignement Technique"),
    ENA("Ecole Nationale d’Agriculture"),
    ENSAM("Ecole Nationale Supérieure d’Arts et Métiers"),
    IUT("Institut Universitaire de Technologie"),
    ISCAE("Institut Supérieur de Commerce et d’Administration des Entreprises"),
    INSA("Institut National des Sciences Appliquées"),
    ENSETFAR("Ecole Nationale Supérieure de l’Enseignement Technique (Far)") , // example variation
    EHTP("Ecole Hassania des Travaux Publics"),
    EMI("Ecole Mohammadia d’Ingénieurs"),
    ECOLE_NATIONALE_SUPERIEURE_D_INFORMATIQUE_ET_D_ANALYSE_DES_SYSTEMES("École Nationale Supérieure d'Informatique et d'Analyse des Systèmes"),
    ECOLE_MOHAMMADIA_D_INGENIEURS("École Mohammadia d'Ingénieurs"),
    ECOLE_NATIONALE_SUPERIEURE_DES_ARTS_ET_METIERS("École Nationale Supérieure des Arts et Métiers"),
    ECOLE_NATIONALE_DE_COMMERCE_ET_DE_GESTION("École Nationale de Commerce et de Gestion"),
    INSTITUT_NATIONAL_DES_SCIENCES_APPLIQUEES("Institut National des Sciences Appliquées"),
    AUTRE("Autre");

    private final String name;

    School(String name) {
        this.name = name;
    }
}
