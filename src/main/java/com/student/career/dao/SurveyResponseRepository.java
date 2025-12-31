package com.student.career.dao;

import com.student.career.bean.SurveyResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SurveyResponseRepository
        extends MongoRepository<SurveyResponse, String> {

    List<SurveyResponse> findBySurveyId(String surveyId);

    Optional<SurveyResponse> findBySurveyIdAndStudentId(
            String surveyId,
            String studentId
    );

    List<SurveyResponse> findByStudentId(String studentId);

    List<SurveyResponse> findByStudentIdIn(List<String> studentIds);
}
