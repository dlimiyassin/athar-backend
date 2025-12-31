package com.student.career.ws.controller;

import com.student.career.service.api.StudentSurveyExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/export")
public class StudentSurveyExportController {

    private final StudentSurveyExportService exportService;

    public StudentSurveyExportController(
            StudentSurveyExportService exportService
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