package com.student.career.ImportExportCsv.service.impl;


import com.student.career.ImportExportCsv.beans.FutureFieldOfStudy;
import com.student.career.ImportExportCsv.dao.FieldOfStudyRepository;
import com.student.career.ImportExportCsv.ws.dto.FutureFieldOfStudyDto;
import com.student.career.ImportExportCsv.service.api.FutureFieldOfStudyService;
import com.student.career.ImportExportCsv.ws.transformer.FutureFieldOfStudyTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FutureFieldOfStudyServiceImpl implements FutureFieldOfStudyService {

    private final FieldOfStudyRepository repository;
    private final FutureFieldOfStudyTransformer transformer;

    @Override
    public List<FutureFieldOfStudyDto> findAllActive() {
        return repository.findAll()
                .stream()
                .filter(FutureFieldOfStudy::isActive)
                .map(transformer::toDto)
                .toList();
    }

    @Override
    public FutureFieldOfStudyDto save(FutureFieldOfStudyDto dto) {
        var entity = transformer.toEntity(dto);
        return transformer.toDto(repository.save(entity));
    }

    @Override
    public FutureFieldOfStudyDto findByCode(Integer code) {
        return repository.findByCode(code)
                .map(transformer::toDto)
                .orElse(null);
    }
}
