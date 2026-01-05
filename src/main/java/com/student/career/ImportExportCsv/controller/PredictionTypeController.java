package com.student.career.ImportExportCsv.controller;


import com.student.career.ImportExportCsv.ws.dto.PredictionTypeDto;
import com.student.career.ImportExportCsv.service.api.PredictionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prediction-types")
@RequiredArgsConstructor
public class PredictionTypeController {

    private final PredictionTypeService service;

    @GetMapping()
    public List<PredictionTypeDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/active")
    public List<PredictionTypeDto> findActive() {
        return service.findAllActive();
    }

    @PostMapping
    public PredictionTypeDto save(@RequestBody PredictionTypeDto dto) {
        return service.save(dto);
    }

    @GetMapping("/code/{code}")
    public PredictionTypeDto findByCode(@PathVariable String code) {
        return service.findByCode(code);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        service.deleteByID(id);
    }
}

