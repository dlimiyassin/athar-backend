package com.student.career.ImportExportCsv.service.api;

import org.springframework.web.multipart.MultipartFile;

public interface SurveyImportService {

    void importPredictionFile(
            MultipartFile file,
            String predictionTypeId
    );
}

