package com.student.career.ImportExportCsv.service.api;

import com.student.career.ImportExportCsv.beans.PredictionResult;

import java.util.List;

public interface PredictionResultService {

    PredictionResult saveOrUpdate(PredictionResult predictionResult);

    List<PredictionResult> findByStudent(String studentId);

    PredictionResult findByStudentAndType(String studentId, String predictionTypeId);

    List<PredictionResult> findByPredictionType(String predictionTypeId);

    long countByPredictionType(String predictionTypeId);

    List<PredictionResult> findAll();

    List<PredictionResult> getByAuthenticatedStudent();
}
