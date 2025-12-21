package com.student.career.service.impl;

import com.student.career.bean.Survey;
import com.student.career.dao.SurveyRepository;
import com.student.career.exception.ResourceNotFoundException;
import com.student.career.service.api.SurveyService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;

    public SurveyServiceImpl(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Override
    public Survey save(Survey survey) {
        if (survey.getCreatedAt() == null) {
            survey.setCreatedAt(Instant.from(LocalDateTime.now()));
        }
        return surveyRepository.save(survey);
    }

    @Override
    public Survey findById(String id) {
        Optional<Survey> found = surveyRepository.findById(id);
        if (found.isEmpty()) {
            throw new ResourceNotFoundException("Survey","Id",id);
        }
        return found.get();
    }

    @Override
    public List<Survey> findByTeacher(String teacherId) {
        return surveyRepository.findByCreatedByTeacherId(teacherId);
    }
}
