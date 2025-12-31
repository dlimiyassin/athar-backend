package com.student.career.bean;

import com.student.career.bean.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AcademicProfile {

    private String gender;

    private Diploma currentDiploma;
    // Dynamic, teacher-defined fields
    private Map<String, Object> customAttributes = new HashMap<>();

    private List<Diploma> diplomas = new ArrayList<>();
}
