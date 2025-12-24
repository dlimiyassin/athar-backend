package com.student.career.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.student.career.bean.enums.TargetType;
import com.student.career.zBase.bean.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Document(collection = "surveys")
public class Survey extends BaseEntity {

    @Id
    private String id;

    private String title;
    private String description;

    private String createdByTeacherId; // User ID

    private List<Question> questions;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createdAt = Instant.now();


    private TargetType target;
}
