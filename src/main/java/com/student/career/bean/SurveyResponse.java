package com.student.career.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Document(collection = "survey_responses")
@CompoundIndexes({
        @CompoundIndex(
                name = "survey_student_unique",
                def = "{'surveyId': 1, 'studentId': 1}",
                unique = true
        )
})
public class SurveyResponse {

    @Id
    private String id;

    private String surveyLabel;
    private String surveyId;
    private String studentId;

    private List<Answer> answers;

    private Instant submittedAt = Instant.now();
}

