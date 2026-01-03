package com.student.career.ImportExportCsv.dao;

import com.student.career.ImportExportCsv.beans.FutureFieldOfStudy;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FieldOfStudyRepository
        extends MongoRepository<FutureFieldOfStudy, String> {

    Optional<FutureFieldOfStudy> findByCode(Integer code);
}
