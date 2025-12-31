package com.student.career.bean.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    final String name;

    Gender(String name){
        this.name = name;
    }
}

