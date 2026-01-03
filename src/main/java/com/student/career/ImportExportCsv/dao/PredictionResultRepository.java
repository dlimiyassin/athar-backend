package com.student.career.ImportExportCsv.dao;

import com.student.career.ImportExportCsv.beans.PredictionResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PredictionResultRepository
        extends MongoRepository<PredictionResult, String> {
}
