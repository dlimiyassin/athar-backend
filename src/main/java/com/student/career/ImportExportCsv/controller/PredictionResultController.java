package com.student.career.ImportExportCsv.controller;

import com.student.career.ImportExportCsv.beans.PredictionResult;
import com.student.career.ImportExportCsv.beans.PredictionType;
import com.student.career.ImportExportCsv.service.api.PredictionResultService;
import com.student.career.ImportExportCsv.service.api.PredictionTypeService;
import com.student.career.ImportExportCsv.ws.dto.PredictionResultDto;
import com.student.career.ImportExportCsv.ws.dto.PredictionTypeDto;
import com.student.career.ImportExportCsv.ws.transformer.PredictionResultTransformer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predictions")
public class PredictionResultController {

    private final PredictionResultService resultService;
    private final PredictionTypeService typeService;
    private final PredictionResultTransformer transformer;

    public PredictionResultController(
            PredictionResultService resultService,
            PredictionTypeService typeService,
            PredictionResultTransformer transformer
    ) {
        this.resultService = resultService;
        this.typeService = typeService;
        this.transformer = transformer;
    }

    /* =========================
       CREATE / UPDATE (CSV Import)
       ========================= */
    @PostMapping
    public ResponseEntity<PredictionResultDto> save(
            @RequestBody PredictionResultDto dto
    ) {
        PredictionResult saved =
                resultService.saveOrUpdate(transformer.toEntity(dto));

        PredictionType predictionType =
                typeService.findById(saved.getPredictionTypeId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transformer.toDto(saved, predictionType));
    }

    /* =========================
       STUDENT PROFILE
       ========================= */

    @GetMapping("/student/{studentId}")
    public List<PredictionResultDto> findByStudent(@PathVariable String studentId) {
        return resultService.findByStudent(studentId)
                .stream()
                .map(pr -> transformer.toDto(
                        pr,
                        typeService.findById(pr.getPredictionTypeId())
                ))
                .toList();
    }

    @GetMapping
    public List<PredictionResultDto> getByAuthenticatedStudent() {
        return resultService.getByAuthenticatedStudent()
                .stream()
                .map(pr -> transformer.toDto(
                        pr,
                        typeService.findById(pr.getPredictionTypeId())
                ))
                .toList();
    }

    @GetMapping("/student/{studentId}/type/{typeId}")
    public PredictionResultDto findByStudentAndType(
            @PathVariable String studentId,
            @PathVariable String typeId
    ) {
        PredictionResult result =
                resultService.findByStudentAndType(studentId, typeId);

        PredictionType type = typeService.findById(typeId);

        return transformer.toDto(result, type);
    }

    /* =========================
       ADMIN / DASHBOARD
       ========================= */

    @GetMapping("/type/{typeId}")
    public List<PredictionResultDto> findByType(
            @PathVariable String typeId
    ) {
        return resultService.findByPredictionType(typeId)
                .stream()
                .map(pr -> transformer.toDto(
                        pr,
                        typeService.findById(pr.getPredictionTypeId())
                ))
                .toList();
    }

    @GetMapping("/type/{typeId}/count")
    public Long countByType(@PathVariable String typeId) {
        return resultService.countByPredictionType(typeId);
    }

    @GetMapping("/all")
    public List<PredictionResultDto> findAll() {
        return resultService.findAll()
                .stream()
                .map(pr -> transformer.toDto(
                        pr,
                        typeService.findById(pr.getPredictionTypeId())
                ))
                .toList();
    }
}
