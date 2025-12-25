package com.student.career.bean.enums;

import lombok.Getter;

@Getter
public enum TargetType {
    ALL("ALL");

    private final String value;

    TargetType(String value) {
        this.value = value;
    }
}
