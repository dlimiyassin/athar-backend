package com.student.career.ImportExportCsv.service.impl;

import com.student.career.ImportExportCsv.beans.PredictionResult;
import com.student.career.ImportExportCsv.dao.PredictionResultRepository;
import com.student.career.ImportExportCsv.service.api.PredictionResultService;
import com.student.career.bean.Student;
import com.student.career.service.api.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PredictionResultServiceImpl implements PredictionResultService {

    private final PredictionResultRepository repository;
    private final StudentService studentService;

    public PredictionResultServiceImpl(PredictionResultRepository repository, StudentService studentService) {
        this.repository = repository;
        this.studentService = studentService;
    }

    @Override
    public PredictionResult saveOrUpdate(PredictionResult predictionResult) {

        repository.findByStudentIdAndPredictionTypeId(
                predictionResult.getStudentId(),
                predictionResult.getPredictionTypeId()
        ).ifPresent(existing -> predictionResult.setId(existing.getId()));

        predictionResult.setGeneratedAt(Instant.now());

        return repository.save(predictionResult);
    }

    @Override
    public List<PredictionResult> findByStudent(String studentId) {
        return repository.findByStudentId(studentId);
    }

    @Override
    public List<PredictionResult> getByAuthenticatedStudent() {
        Optional<Student> student = studentService.findAuthenticatedStudent();
        if (student.isPresent()) {
            return findByStudent(student.get().getId());
        }
        return List.of();
    }


    @Override
    public PredictionResult findByStudentAndType(String studentId, String predictionTypeId) {
        return repository.findByStudentIdAndPredictionTypeId(studentId, predictionTypeId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Prediction not found for student and type"));
    }

    @Override
    public List<PredictionResult> findByPredictionType(String predictionTypeId) {
        return repository.findByPredictionTypeId(predictionTypeId);
    }

    @Override
    public long countByPredictionType(String predictionTypeId) {
        return repository.countByPredictionTypeId(predictionTypeId);
    }

    @Override
    public List<PredictionResult> findAll() {
        return repository.findAll();
    }
}

