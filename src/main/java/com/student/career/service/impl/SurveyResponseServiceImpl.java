package com.student.career.service.impl;

import com.student.career.bean.Student;
import com.student.career.bean.SurveyResponse;
import com.student.career.dao.SurveyResponseRepository;
import com.student.career.exception.ResourceNotFoundException;
import com.student.career.service.api.StudentService;
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
    private final StudentService studentService;

    public SurveyResponseServiceImpl(
            SurveyResponseRepository repository, StudentService studentService
    ) {
        this.repository = repository;
        this.studentService = studentService;
    }

    @Override
    public SurveyResponse submit(SurveyResponse response) {
        Optional<Student> student = studentService.findAuthenticatedStudent();
        if (student.isEmpty()){
            throw new ResourceNotFoundException("Student","Id", 0L);
        }
        response.setStudentId(student.get().getId());
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
    public List<SurveyResponse> findByStudent(String studentId) {
        return repository.findByStudentId(studentId);
    }

    @Override
    public List<SurveyResponse> findByStudent() {
        Optional<Student> student = studentService.findAuthenticatedStudent();
        if (student.isEmpty()){
            throw new ResourceNotFoundException("Student","Id", 0L);
        }
        return repository.findByStudentId(student.get().getId());
    }

    @Override
    public List<SurveyResponse> findBySurvey(String surveyId) {
        return repository.findBySurveyId(surveyId);
    }
}
