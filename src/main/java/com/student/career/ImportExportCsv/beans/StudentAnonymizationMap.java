package com.student.career.ImportExportCsv.beans;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "student_anonymization_map")
@Getter
@Setter
@CompoundIndexes({
        @CompoundIndex(
                name = "anon_student_unique",
                def = "{'anonymousId': 1}",
                unique = true
        )
})
public class StudentAnonymizationMap {

    @Id
    private String id;

    private String studentId;     // REAL ID (internal only)
    private String anonymousId;   // HASHED ID used in CSV
}

