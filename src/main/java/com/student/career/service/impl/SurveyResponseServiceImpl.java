package com.student.career.service.impl;

import com.student.career.bean.SurveyResponse;
import com.student.career.dao.SurveyResponseRepository;
import com.student.career.service.api.SurveyResponseService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyResponseServiceImpl
        implements SurveyResponseService {

    private final SurveyResponseRepository repository;

    public SurveyResponseServiceImpl(
            SurveyResponseRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public SurveyResponse submit(SurveyResponse response) {
        if (response.getSubmittedAt() == null) {
            response.setSubmittedAt(Instant.from(LocalDateTime.now()));
        }
        return repository.save(response);
    }

    @Override
    public Optional<SurveyResponse> findBySurveyAndStudent(
            String surveyId,
            String studentId
    ) {
        return repository.findBySurveyIdAndStudentId(
                surveyId,
                studentId
        );
    }

    @Override
    public List<SurveyResponse> findBySurvey(String surveyId) {
        return repository.findBySurveyId(surveyId);
    }
}
