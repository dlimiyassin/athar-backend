package com.student.career.bean;

import com.student.career.zBase.bean.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;


@Getter
@Setter
@Document(collection = "students")
@CompoundIndexes({
        @CompoundIndex(name = "user_id_idx", def = "{'userId': 1}")
})
public class Student  extends BaseEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userId; // reference to User

    private AcademicProfile academicProfile;
}
