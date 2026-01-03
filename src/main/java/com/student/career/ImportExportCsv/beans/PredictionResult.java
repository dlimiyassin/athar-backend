package com.student.career.ImportExportCsv.beans;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "prediction_results")
@Getter @Setter
@CompoundIndexes({
        @CompoundIndex(
                name = "student_prediction_unique",
                def = "{'studentId': 1, 'predictionTypeId': 1}"
        )
})
public class PredictionResult {

    @Id
    private String id;

    private String studentId;         // REAL student id (internal)
    private String predictionTypeId;  // FK → PredictionType

    private String rawValue;          // value from CSV (stringified)
    private String interpretedValue;  // final readable value

    private Instant generatedAt = Instant.now();
    private String modelVersion;      // optional but STRONGLY recommended
}

