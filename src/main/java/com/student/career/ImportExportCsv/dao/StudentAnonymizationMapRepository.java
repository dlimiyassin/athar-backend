package com.student.career.ImportExportCsv.dao;

import com.student.career.ImportExportCsv.beans.StudentAnonymizationMap;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentAnonymizationMapRepository
        extends MongoRepository<StudentAnonymizationMap, String> {

    Optional<StudentAnonymizationMap> findByAnonymousId(String anonymousId);
}
