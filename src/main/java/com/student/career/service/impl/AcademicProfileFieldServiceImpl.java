package com.student.career.service.impl;

import com.student.career.bean.AcademicProfileField;
import com.student.career.dao.AcademicProfileFieldRepository;
import com.student.career.service.api.AcademicProfileFieldService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AcademicProfileFieldServiceImpl
        implements AcademicProfileFieldService {

    private final AcademicProfileFieldRepository repository;

    public AcademicProfileFieldServiceImpl(
            AcademicProfileFieldRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public AcademicProfileField save(AcademicProfileField field) {
        return repository.save(field);
    }

    @Override
    public List<AcademicProfileField> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<AcademicProfileField> findByName(String name) {
        return repository.findByName(name);
    }
}
