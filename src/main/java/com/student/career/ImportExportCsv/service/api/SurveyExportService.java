package com.student.career.ImportExportCsv.service.api;

import jakarta.servlet.http.HttpServletResponse;

public interface SurveyExportService {

    void exportStudentsSurveyCsv(HttpServletResponse response);
}

