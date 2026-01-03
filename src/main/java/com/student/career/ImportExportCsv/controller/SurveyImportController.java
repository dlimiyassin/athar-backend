package com.student.career.ImportExportCsv.controller;

import com.student.career.ImportExportCsv.service.api.SurveyImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/survey/import")
public class SurveyImportController {

    private final SurveyImportService importService;

    public SurveyImportController(SurveyImportService importService) {
        this.importService = importService;
    }

    @PostMapping("/predictions")
    public ResponseEntity<Void> importPredictions(
            @RequestParam("file") MultipartFile file,
            @RequestParam("predictionTypeId") String predictionTypeId
    ) {
        importService.importPredictionFile(file, predictionTypeId);
        return ResponseEntity.ok().build();
    }
}
