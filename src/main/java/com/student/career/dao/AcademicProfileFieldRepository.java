package com.student.career.dao;

import com.student.career.bean.AcademicProfileField;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AcademicProfileFieldRepository
        extends MongoRepository<AcademicProfileField, String> {

    Optional<AcademicProfileField> findByName(String name);
}
