package com.student.career.ImportExportCsv.service.impl;

import com.student.career.ImportExportCsv.beans.FutureFieldOfStudy;
import com.student.career.ImportExportCsv.beans.PredictionResult;
import com.student.career.ImportExportCsv.beans.PredictionType;
import com.student.career.ImportExportCsv.dao.FieldOfStudyRepository;
import com.student.career.ImportExportCsv.dao.PredictionResultRepository;
import com.student.career.ImportExportCsv.dao.PredictionTypeRepository;
import com.student.career.ImportExportCsv.service.api.SurveyImportService;
import com.student.career.zBase.util.AnonymizationUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Service
public class SurveyImportServiceImpl
        implements SurveyImportService {

    private final PredictionTypeRepository predictionTypeRepository;
    private final PredictionResultRepository predictionResultRepository;
    private final FieldOfStudyRepository fieldOfStudyRepository;
    private final AnonymizationUtil anonymizationUtil;

    public SurveyImportServiceImpl(
            PredictionTypeRepository predictionTypeRepository,
            PredictionResultRepository predictionResultRepository,
            FieldOfStudyRepository fieldOfStudyRepository,
            AnonymizationUtil anonymizationUtil
    ) {
        this.predictionTypeRepository = predictionTypeRepository;
        this.predictionResultRepository = predictionResultRepository;
        this.fieldOfStudyRepository = fieldOfStudyRepository;
        this.anonymizationUtil = anonymizationUtil;
    }

    @Override
    public void importPredictionFile(
            MultipartFile file,
            String predictionTypeId
    ) {

        PredictionType type = predictionTypeRepository
                .findById(predictionTypeId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid prediction type: " + predictionTypeId)
                );

        int rowNumber = 1; // header = row 1

        try (
                Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
                CSVParser parser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .parse(reader)
        ) {

            for (CSVRecord record : parser) {
                rowNumber = (int) record.getRecordNumber() + 1;

                String anonStudentId = record.get("student_id");
                String predictionValue = record.get(record.size() - 1);

                String realStudentId =
                        anonymizationUtil.resolveStudentId(anonStudentId);

                PredictionResult result = new PredictionResult();
                result.setStudentId(realStudentId);
                result.setPredictionTypeId(type.getId());
                result.setRawValue(predictionValue);
                result.setInterpretedValue(
                        interpretValue(type, predictionValue)
                );

                predictionResultRepository.save(result);
            }

        } catch (IllegalArgumentException e) {
            // Business / validation errors
            throw new RuntimeException(
                    "Import failed at row " + rowNumber + ": " + e.getMessage(),
                    e
            );

        } catch (Exception e) {
            // Parsing / IO / unexpected errors
            throw new RuntimeException(
                    "Import failed at row " + rowNumber + " due to: " + e.getClass().getSimpleName()
                            + " - " + e.getMessage(),
                    e
            );
        }
    }


    private String interpretValue(
            PredictionType type,
            String raw
    ) {

        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("Prediction value is empty");
        }

        String value = raw.trim();

        return switch (type.getValueType()) {

            case BOOLEAN -> {
                if (value.equals("1")) {
                    yield "YES";
                }
                if (value.equals("0")) {
                    yield "NO";
                }
                throw new IllegalArgumentException(
                        "Invalid "+type.getLabel()+" value: '" + raw + "' (expected 0 or 1)"
                );
            }

            case NUMBER -> {
                try {
                    // validation only, value kept as string
                    Double.parseDouble(value);
                    yield value;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            "Invalid "+type.getLabel()+" value: '" + raw + "'",
                            e
                    );
                }
            }

            case CATEGORY -> {
                int code;
                try {
                    code = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            "Invalid "+type.getLabel()+" code: '" + raw + "'",
                            e
                    );
                }

                yield fieldOfStudyRepository
                        .findByCode(code)
                        .map(FutureFieldOfStudy::getLabel)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Unknown "+type.getLabel()+" code: " + code
                                )
                        );
            }
        };
    }

}

