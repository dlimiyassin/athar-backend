package com.student.career.bean.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum University {
    HASSAN_2("Université Hassan II"),
    SOLTAN_MOLAY_SLIMANE("Université Sultan Moulay Slimane"),
    IBN_TOFAIL("Université Ibn Tofail"),
    CADI_AYYAD("Université Cadi Ayyad"),
    SIDI_MOHAMED_BEN_ABDELLAH("Université Sidi Mohamed Ben Abdellah"),
    MOHAMMED_5("Université Mohammed V"),
    MOHAMMED_6_POLYTECHNIQUE("Université Mohammed VI Polytechnique"),
    AL_AKHAWAYN("Université Al Akhawayn"),
    ABDELMALEK_ESSAADI("Université Abdelmalek Essaadi"),
    CHOUAIB_DOUKKALI("Université Chouaib Doukkali"),
    MOHAMMED_PREMIER("Université Mohammed Premier"),
    IBN_ZOHR("Université Ibn Zohr"),
    HASSAN_PREMIER("Université Hassan Premier"),
    MOULAY_ISMAIL("Université Moulay Ismail"),
    UNIVERSITE_INTERNATIONALE_DE_RABAT("Université Internationale de Rabat"),
    UNIVERSITE_PRIVEE_DE_MARRAKECH("Université Privée de Marrakech"),
    UNIVERSITE_EURO_MEDITERRANEENNE_DE_FES("Université Euro-Méditerranéenne de Fès"),
    UNIVERSITE_POLYDISCIPLINAIRE_DE_BENI_MELLAL("Université Polydisciplinaire de Beni Mellal"),
    UNIVERSITE_POLYDISCIPLINAIRE_DE_KHOURIBGA("Université Polydisciplinaire de Khouribga"),
    UNIVERSITE_POLYDISCIPLINAIRE_DE_LARACHE("Université Polydisciplinaire de Larache"),
    UNIVERSITE_POLYDISCIPLINAIRE_DE_SAFI("Université Polydisciplinaire de Safi"),
    UNIVERSITE_POLYDISCIPLINAIRE_DE_TAZA("Université Polydisciplinaire de Taza"),
    AUTRE("Autre");

    private final String nom;

    University(String nom) {
        this.nom = nom;
    }

    // Optional: Method to get enum from string
    public static University fromNom(String nom) {
        for (University university : values()) {
            if (university.nom.equalsIgnoreCase(nom)) {
                return university;
            }
        }
        return AUTRE; // Default to AUTRE if not found
    }

    // Optional: Get all names as list
    public static List<String> getAllNoms() {
        return Arrays.stream(values())
                .map(University::getNom)
                .collect(Collectors.toList());
    }

    // Optional: Get enum values as map
    public static Map<String, String> asMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (University university : values()) {
            map.put(university.name(), university.getNom());
        }
        return map;
    }
}