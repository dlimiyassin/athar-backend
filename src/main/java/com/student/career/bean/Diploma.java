package com.student.career.bean;

import com.student.career.bean.enums.StudyLevel;
import com.student.career.bean.enums.University;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Diploma {
    private University university;
    private String school;
    private StudyLevel studyLevel;
    private String studyField;
    private String title;
    private Integer year;
    private Double grade;
}
