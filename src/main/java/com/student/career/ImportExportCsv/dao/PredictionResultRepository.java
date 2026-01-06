package com.student.career.ImportExportCsv.dao;

import com.student.career.ImportExportCsv.beans.PredictionResult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PredictionResultRepository
        extends MongoRepository<PredictionResult, String> {

    List<PredictionResult> findByStudentId(String studentId);

    List<PredictionResult> findByPredictionTypeId(String predictionTypeId);

    Optional<PredictionResult> findByStudentIdAndPredictionTypeId(
            String studentId,
            String predictionTypeId
    );

    long countByPredictionTypeId(String predictionTypeId);
}

