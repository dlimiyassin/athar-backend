package com.student.career.ImportExportCsv.controller;


import com.student.career.ImportExportCsv.ws.dto.PredictionTypeDto;
import com.student.career.ImportExportCsv.service.api.PredictionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.student.career.ImportExportCsv.beans.PredictionType;
import com.student.career.ImportExportCsv.ws.transformer.PredictionTypeTransformer;

@RestController
@RequestMapping("/api/prediction-types")
@RequiredArgsConstructor
public class PredictionTypeController {

    private final PredictionTypeService service;
    private final PredictionTypeTransformer transformer;

    @GetMapping
    public List<PredictionTypeDto> findAll() {
        return service.findAll()
                .stream()
                .map(transformer::toDto)
                .toList();
    }

    @GetMapping("/active")
    public List<PredictionTypeDto> findActive() {
        return service.findAllActive()
                .stream()
                .map(transformer::toDto)
                .toList();
    }

    @PostMapping
    public PredictionTypeDto save(@RequestBody PredictionTypeDto dto) {
        PredictionType entity = transformer.toEntity(dto);
        return transformer.toDto(service.save(entity));
    }

    @GetMapping("/code/{code}")
    public PredictionTypeDto findByCode(@PathVariable String code) {
        return transformer.toDto(service.findByCode(code));
    }

    @GetMapping("/{id}")
    public PredictionTypeDto findById(@PathVariable String id) {
        return transformer.toDto(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        service.deleteByID(id);
    }
}

