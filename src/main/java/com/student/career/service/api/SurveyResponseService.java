package com.student.career.service.api;

import com.student.career.bean.SurveyResponse;

import java.util.List;
import java.util.Optional;

public interface SurveyResponseService {

    SurveyResponse submit(SurveyResponse response);

    Optional<SurveyResponse> findBySurveyAndStudent(
            String surveyId,
            String studentId
    );

    List<SurveyResponse> findByStudent(String studentId);

    List<SurveyResponse> findByStudent();

    List<SurveyResponse> findBySurvey(String surveyId);

}