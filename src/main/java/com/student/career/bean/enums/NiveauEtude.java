package com.student.career.bean.enums;

import lombok.Getter;

@Getter
public enum NiveauEtude {
    BAC("Baccalauréat"),
    BAC_PLUS_1("BAC+1"),
    BAC_PLUS_2("BAC+2"),
    BAC_PLUS_3("BAC+3"),
    BAC_PLUS_4("BAC+4"),
    BAC_PLUS_5("BAC+5"),
    BAC_PLUS_6("BAC+6"),
    BAC_PLUS_7("BAC+7"),
    BAC_PLUS_8("BAC+8");

    private final String label;

    NiveauEtude(String label) {
        this.label = label;
    }

}