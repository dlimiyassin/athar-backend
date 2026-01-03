package com.student.career.ImportExportCsv.controller;

import com.student.career.ImportExportCsv.service.api.SurveyExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/survey/export")
public class SurveyExportController {

    private final SurveyExportService exportService;

    public SurveyExportController(
            SurveyExportService exportService
    ) {
        this.exportService = exportService;
    }

    @GetMapping("/students-surveys")
    public void exportStudentsSurveysCsv(
            HttpServletResponse response
    ) {
        exportService.exportStudentsSurveyCsv(response);
    }
}