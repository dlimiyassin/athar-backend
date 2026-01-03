package com.student.career.ImportExportCsv.service.impl;

import com.student.career.ImportExportCsv.beans.PredictionType;
import com.student.career.ImportExportCsv.dao.PredictionTypeRepository;
import com.student.career.ImportExportCsv.ws.dto.PredictionTypeDto;
import com.student.career.ImportExportCsv.service.api.PredictionTypeService;
import com.student.career.ImportExportCsv.ws.transformer.PredictionTypeTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionTypeServiceImpl implements PredictionTypeService {

    private final PredictionTypeRepository repository;
    private final PredictionTypeTransformer transformer;

    @Override
    public List<PredictionTypeDto> findAllActive() {
        return repository.findAll()
                .stream()
                .filter(PredictionType::isActive)
                .map(transformer::toDto)
                .toList();
    }

    @Override
    public PredictionTypeDto save(PredictionTypeDto dto) {
        var entity = transformer.toEntity(dto);
        return transformer.toDto(repository.save(entity));
    }

    @Override
    public PredictionTypeDto findByCode(String code) {
        return repository.findByCode(code)
                .map(transformer::toDto)
                .orElse(null);
    }
}

