package com.student.career.ImportExportCsv.service.impl;


import com.student.career.ImportExportCsv.beans.FutureFieldOfStudy;
import com.student.career.ImportExportCsv.dao.FieldOfStudyRepository;
import com.student.career.ImportExportCsv.ws.dto.FutureFieldOfStudyDto;
import com.student.career.ImportExportCsv.service.api.FutureFieldOfStudyService;
import com.student.career.ImportExportCsv.ws.transformer.FutureFieldOfStudyTransformer;
import com.student.career.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FutureFieldOfStudyServiceImpl implements FutureFieldOfStudyService {

    private final FieldOfStudyRepository repository;
    private final FutureFieldOfStudyTransformer transformer;

    @Override
    public List<FutureFieldOfStudyDto> findAll() {
        return repository.findAll()
                .stream()
                .map(transformer::toDto)
                .toList();
    }

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

    @Override
    public void deleteByID(String id){
        if (this.repository.findById(id).isEmpty()) throw new ResourceNotFoundException("Field Of Study","Id",id);
        this.repository.deleteById(id);
    }
}
