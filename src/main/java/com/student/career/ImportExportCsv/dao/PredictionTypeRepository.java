package com.student.career.ImportExportCsv.dao;

import com.student.career.ImportExportCsv.beans.PredictionType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PredictionTypeRepository
        extends MongoRepository<PredictionType, String> {

    Optional<PredictionType> findByCode(String code);
}
