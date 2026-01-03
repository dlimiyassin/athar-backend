package com.student.career.ImportExportCsv.beans;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "fields_of_study")
@Getter @Setter
public class FutureFieldOfStudy {

    @Id
    private String id;

    private Integer code;   // number from AI (you assign)
    private String label;   // "Computer Science"
    private boolean active;
}

