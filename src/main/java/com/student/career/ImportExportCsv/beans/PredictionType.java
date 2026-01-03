package com.student.career.ImportExportCsv.beans;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "prediction_types")
@Getter
@Setter
public class PredictionType {

    @Id
    private String id;

    private String code;        // FIELD_ORIENTATION | JOB_ELIGIBILITY | GRADE_PREDICTION
    private String label;       // UI label
    private PredictionValueType valueType; // NUMBER | BOOLEAN | CATEGORY
    private String description;
    private boolean active;
}
