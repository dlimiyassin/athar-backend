package com.student.career.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AcademicProfile {

    private String ecole;
    private String program;
    private Integer year;
    // Dynamic, teacher-defined fields
    private Map<String, Object> customAttributes = new HashMap<>();

    private List<Diplome> diplomes = new ArrayList<>();
}
