package com.student.career.bean;

import com.student.career.bean.enums.FieldType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "academic_profile_fields")
@CompoundIndexes({
        @CompoundIndex(name = "field_name_unique", def = "{'name': 1}", unique = true)
})
public class AcademicProfileField {

    @Id
    private String id;

    private String name;        // e.g. "gpa"
    private String label;       // e.g. "Grade Point Average"
    private FieldType type;     // TEXT, NUMBER, BOOLEAN
    private boolean required;
}
