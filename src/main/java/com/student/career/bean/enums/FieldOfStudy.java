package com.student.career.bean.enums;

import lombok.Getter;

@Getter
public enum FieldOfStudy {

    // Bac Options
    SCIENCES_MATH_A("Sciences Mathématiques A"),
    SCIENCES_MATH_B("Sciences Mathématiques B"),
    SCIENCES_PHYSIQUES("Sciences Physiques"),
    SVT("Sciences de la Vie et de la Terre"),
    SCIENCES_ECO_SOCIALES("Sciences Économiques et Sociales"),
    LETTRES_SCIENCES_HUMAINES("Lettres et Sciences Humaines"),
    LANGUES("Langues"),
    SCIENCES_EXPERIMENTALES("Sciences Expérimentales"),
    STMG("Sciences et Technologies du Management et de la Gestion"),
    BAC_PRO_COMMERCE("Baccalauréat Professionnel - Commerce"),
    BAC_PRO_TECHNIQUE("Baccalauréat Professionnel - Technique"),

    // Science & Technology
    MATHEMATICS("Mathematics"),
    PHYSICS("Physics"),
    CHEMISTRY("Chemistry"),
    BIOLOGY("Biology"),
    COMPUTER_SCIENCE("Computer Science"),
    ELECTRONICS("Electronics"),
    MECHANICAL_ENGINEERING("Mechanical Engineering"),
    CIVIL_ENGINEERING("Civil Engineering"),
    ELECTRICAL_ENGINEERING("Electrical Engineering"),
    SOFTWARE_ENGINEERING("Software Engineering"),
    INDUSTRIAL_ENGINEERING("Industrial Engineering"),
    TELECOMMUNICATIONS("Telecommunications"),

    // Business, Economics, Management
    ECONOMICS("Economics"),
    MANAGEMENT("Management"),
    BUSINESS("Business"),
    FINANCE("Finance"),
    ACCOUNTING("Accounting"),
    MARKETING("Marketing"),
    LOGISTICS("Logistics and Transport"),

    // Health
    MEDICINE("Medicine"),
    PHARMACY("Pharmacy"),
    DENTISTRY("Dentistry"),
    NURSING("Nursing"),
    PUBLIC_HEALTH("Public Health"),

    // Law & Political Science
    LAW("Law"),
    POLITICAL_SCIENCE("Political Science"),
    INTERNATIONAL_RELATIONS("International Relations"),

    // Education & Social Sciences
    EDUCATION("Education Sciences"),
    PSYCHOLOGY("Psychology"),
    SOCIOLOGY("Sociology"),

    // Humanities & Languages
    LITERATURE("Literature"),
    LANGUAGES("Languages"),
    HISTORY("History"),
    GEOGRAPHY("Geography"),
    PHILOSOPHY("Philosophy"),

    // Agriculture & Environment
    AGRICULTURE("Agriculture"),
    AGRONOMY("Agronomy"),
    ENVIRONMENT("Environment"),
    FOOD_SCIENCE("Agri-Food Science"),

    // Modern Digital Fields
    DATA_SCIENCE("Data Science"),
    ARTIFICIAL_INTELLIGENCE("Artificial Intelligence"),
    CYBERSECURITY("Cybersecurity"),

    OTHER("Other");

    private final String label;

    FieldOfStudy(String label) {
        this.label = label;
    }
}
