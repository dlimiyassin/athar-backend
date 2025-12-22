package com.student.career.service.api;

import com.student.career.bean.AcademicProfileField;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface AcademicProfileFieldService {

    AcademicProfileField save(AcademicProfileField field);

    List<AcademicProfileField> findAll();

    Optional<AcademicProfileField> findByName(String name);

    AcademicProfileField update(@Valid AcademicProfileField entity);

    AcademicProfileField deleteByName(String name);
}