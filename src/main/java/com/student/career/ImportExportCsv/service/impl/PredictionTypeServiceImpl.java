package com.student.career.ImportExportCsv.service.impl;

import com.student.career.ImportExportCsv.beans.PredictionType;
import com.student.career.ImportExportCsv.dao.PredictionTypeRepository;
import com.student.career.ImportExportCsv.ws.dto.PredictionTypeDto;
import com.student.career.ImportExportCsv.service.api.PredictionTypeService;
import com.student.career.ImportExportCsv.ws.transformer.PredictionTypeTransformer;
import com.student.career.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionTypeServiceImpl implements PredictionTypeService {

    private final PredictionTypeRepository repository;

    @Override
    public List<PredictionType> findAll() {
        return repository.findAll();
    }

    @Override
    public List<PredictionType> findAllActive() {
        return repository.findAll()
                .stream()
                .filter(PredictionType::isActive)
                .toList();
    }

    @Override
    public PredictionType save(PredictionType entity) {
        return repository.save(entity);
    }

    @Override
    public PredictionType findByCode(String code) {
        return repository.findByCode(code).orElse(null);
    }

    @Override
    public PredictionType findById(String id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("PredictionType", "id", id)
                );
    }

    @Override
    public void deleteByID(String id) {
        if (repository.findById(id).isEmpty())
            throw new ResourceNotFoundException("PredictionType", "id", id);

        repository.deleteById(id);
    }
}
