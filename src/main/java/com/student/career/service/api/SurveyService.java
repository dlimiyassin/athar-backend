package com.student.career.service.api;

import com.student.career.bean.Survey;

import java.util.List;
import java.util.Optional;

public interface SurveyService {

    Survey save(Survey survey);

    Survey findById(String id);

    List<Survey> findByTeacher(String teacherId);
}
