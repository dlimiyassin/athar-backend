package com.student.career.bean;

import com.student.career.bean.enums.QuestionType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Question {

    private String id;
    private QuestionType type;
    private String label;

    private List<String> options;
}
