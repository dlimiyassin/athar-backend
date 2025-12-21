package com.student.career.ws.transformer;

import com.student.career.bean.Student;
import com.student.career.ws.dto.StudentDto;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.dao.facade.UserDao;
import com.student.career.zBase.security.ws.transformer.UserTransformer;
import com.student.career.zBase.transformer.AbstractTransformer;
import org.springframework.stereotype.Component;

@Component
public class StudentTransformer
        extends AbstractTransformer<Student, StudentDto> {

    private final AcademicProfileTransformer academicProfileTransformer;
    private final UserTransformer userTransformer;

    public StudentTransformer(
            AcademicProfileTransformer academicProfileTransformer,
            UserTransformer userTransformer
    ) {
        this.academicProfileTransformer = academicProfileTransformer;
        this.userTransformer = userTransformer;
    }

    /**
     * DTO → Entity
     * Full UserDto accepted, ONLY userId persisted
     */
    @Override
    public Student toEntity(StudentDto dto) {
        if (dto == null) {
            return null;
        }

        Student student = new Student();
        student.setId(dto.id());

        if (dto.user() != null) {
            student.setUserId(dto.user().id());
        }

        student.setAcademicProfile(
                academicProfileTransformer.toEntity(dto.academicProfile())
        );

        return student;
    }

    /**
     * Entity → DTO (light)
     * Used when User is NOT needed
     */
    @Override
    public StudentDto toDto(Student entity) {
        if (entity == null) {
            return null;
        }

        return new StudentDto(
                entity.getId(),
                null, // intentionally null
                academicProfileTransformer.toDto(entity.getAcademicProfile())
        );
    }

    /**
     * Entity → DTO (enriched)
     * Full User object provided by SERVICE layer
     */
    public StudentDto toDto(Student entity, User user) {
        if (entity == null) {
            return null;
        }

        return new StudentDto(
                entity.getId(),
                userTransformer.toDto(user),
                academicProfileTransformer.toDto(entity.getAcademicProfile())
        );
    }
}
