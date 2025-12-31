package com.student.career.service.impl;

import com.student.career.bean.Student;
import com.student.career.bean.Survey;
import com.student.career.bean.SurveyResponse;
import com.student.career.dao.SurveyRepository;
import com.student.career.exception.ResourceNotFoundException;
import com.student.career.service.api.StudentService;
import com.student.career.service.api.SurveyResponseService;
import com.student.career.service.api.SurveyService;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.service.facade.UserService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserService userService;
    private final StudentService studentService;
    private final SurveyResponseService surveyResponseService;

    public SurveyServiceImpl(SurveyRepository surveyRepository, UserService userService, StudentService studentService, SurveyResponseService surveyResponseService) {
        this.surveyRepository = surveyRepository;
        this.userService = userService;
        this.studentService = studentService;
        this.surveyResponseService = surveyResponseService;
    }

    @Override
    public List<Survey> findAll() {
        return surveyRepository.findAll();
    }

    @Override
    public Survey save(Survey survey) {
        if (survey.getCreatedAt() == null) {
            survey.setCreatedAt(Instant.from(LocalDateTime.now()));
        }
        if(survey.getCreatedByTeacherId().isBlank()){
            User teacher = userService.loadAuthenticatedUser();
            survey.setCreatedByTeacherId(teacher.getId());
        }
        return surveyRepository.save(survey);
    }

    @Override
    public Survey update(Survey entity) {
        Survey existingSurvey = findById(entity.getId());

        if (entity.getTitle() != null) {
            existingSurvey.setTitle(entity.getTitle());
        }
        if (entity.getDescription() != null) {
            existingSurvey.setDescription(entity.getDescription());
        }
        if (entity.getQuestions() != null) {
            existingSurvey.setQuestions(entity.getQuestions());
        }
        if (entity.getTarget() != null) {
            existingSurvey.setTarget(entity.getTarget());
        }

        return surveyRepository.save(existingSurvey);
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
    public List<Survey> findByTeacher() {
        User teacher = userService.loadAuthenticatedUser();
        return surveyRepository.findByCreatedByTeacherId(teacher.getId());
    }

    @Override
    public List<Survey> findSurveyNotAnsweredByStudent() {
        Student student = studentService.findAuthenticatedStudent()
                .orElseThrow(() -> new ResourceNotFoundException("Student", "Id", 0L));

        List<SurveyResponse> surveyResponses = surveyResponseService.findByStudent(student.getId());

        List<String> answeredSurveyIds = surveyResponses.stream()
                .map(SurveyResponse::getSurveyId)
                .collect(Collectors.toList());

        if (answeredSurveyIds.isEmpty()) {
            return surveyRepository.findAll();
        }

        return surveyRepository.findByIdNotIn(answeredSurveyIds);
    }

    @Override
    public List<Survey> findSurveyAnsweredByStudent() {
        Student student = studentService.findAuthenticatedStudent()
                .orElseThrow(() -> new ResourceNotFoundException("Student", "Id", 0L));

        List<SurveyResponse> surveyResponses = surveyResponseService.findByStudent(student.getId());

        List<String> answeredSurveyIds = surveyResponses.stream()
                .map(SurveyResponse::getSurveyId)
                .collect(Collectors.toList());

        if (answeredSurveyIds.isEmpty()) {
            return List.of();
        }

        return surveyRepository.findByIdIn(answeredSurveyIds);
    }

    @Override
    public void deleteByID(String surveyId) {
        this.surveyRepository.deleteById(surveyId);
    }
}
