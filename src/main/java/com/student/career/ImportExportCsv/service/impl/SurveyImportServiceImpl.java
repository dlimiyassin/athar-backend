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
                .orElseThrow(() -> new IllegalArgumentException("Invalid type"));

        try (
                Reader reader = new InputStreamReader(file.getInputStream());
                CSVParser parser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .parse(reader)
        ) {

            for (CSVRecord record : parser) {

                String anonStudentId = record.get("student_id");
                String predictionValue = record.get(
                        record.size() - 1
                ); // last column = AI output

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

        } catch (Exception e) {
            throw new RuntimeException("Import failed", e);
        }
    }

    private String interpretValue(
            PredictionType type,
            String raw
    ) {

        return switch (type.getValueType()) {

            case BOOLEAN -> raw.equals("1") ? "YES" : "NO";

            case NUMBER -> raw;

            case CATEGORY -> {
                int code = Integer.parseInt(raw);
                yield fieldOfStudyRepository
                        .findByCode(code)
                        .map(FutureFieldOfStudy::getLabel)
                        .orElse("Unknown");
            }
        };
    }
}

