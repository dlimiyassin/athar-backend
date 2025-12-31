package com.student.career.service.api;

import jakarta.servlet.http.HttpServletResponse;

public interface StudentSurveyExportService {

    void exportStudentsSurveyCsv(HttpServletResponse response);
}

