package com.student.career.ws.controller;

import com.student.career.bean.AcademicProfileField;
import com.student.career.service.api.AcademicProfileFieldService;
import com.student.career.ws.dto.AcademicProfileFieldDto;
import com.student.career.ws.transformer.AcademicProfileFieldTransformer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic-profile-fields")
public class AcademicProfileFieldController {

    private final AcademicProfileFieldService fieldService;
    private final AcademicProfileFieldTransformer transformer;

    public AcademicProfileFieldController(
            AcademicProfileFieldService fieldService,
            AcademicProfileFieldTransformer transformer
    ) {
        this.fieldService = fieldService;
        this.transformer = transformer;
    }

    @PostMapping
    public AcademicProfileFieldDto create(
            @RequestBody AcademicProfileFieldDto dto
    ) {
        AcademicProfileField saved =
                fieldService.save(transformer.toEntity(dto));
        return transformer.toDto(saved);
    }

    @GetMapping
    public List<AcademicProfileFieldDto> findAll() {
        return fieldService.findAll()
                .stream()
                .map(transformer::toDto)
                .toList();
    }
}
