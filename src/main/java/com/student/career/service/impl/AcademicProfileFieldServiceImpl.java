package com.student.career.service.impl;

import com.student.career.bean.AcademicProfileField;
import com.student.career.dao.AcademicProfileFieldRepository;
import com.student.career.exception.ResourceNotFoundException;
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


    @Override
    public AcademicProfileField update(AcademicProfileField academicProfileField) {

        AcademicProfileField entity = repository.findByName(academicProfileField.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "AcademicProfileField", "name", academicProfileField.getName()));

        // Do NOT allow name change
        entity.setLabel(academicProfileField.getLabel());
        entity.setType(academicProfileField.getType());
        entity.setRequired(academicProfileField.isRequired());

        return repository.save(entity);
    }

    @Override
    public AcademicProfileField deleteByName(String name) {

        AcademicProfileField entity = repository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "AcademicProfileField", "name", name));

        repository.delete(entity);
        return entity;
    }
}
