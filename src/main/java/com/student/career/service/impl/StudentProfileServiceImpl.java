package com.student.career.service.impl;

import com.student.career.bean.AcademicProfile;
import com.student.career.bean.AcademicProfileField;
import com.student.career.bean.Diplome;
import com.student.career.bean.Student;
import com.student.career.dao.StudentRepository;
import com.student.career.service.api.AcademicProfileFieldService;
import com.student.career.service.api.StudentProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentRepository studentRepository;
    private final AcademicProfileFieldService academicProfileFieldService;

    public StudentProfileServiceImpl(
            StudentRepository studentRepository,
            AcademicProfileFieldService academicProfileFieldService
    ) {
        this.studentRepository = studentRepository;
        this.academicProfileFieldService = academicProfileFieldService;
    }

    @Override
    public Student getByUserId(String userId) {
        return studentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Student not found for userId: " + userId)
                );
    }

    @Override
    public Student updateAcademicProfile(
            String studentId,
            AcademicProfile academicProfile
    ) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Student not found with id: " + studentId)
                );

        validateAcademicProfile(academicProfile);
        validateDiplomes(academicProfile);


        student.setAcademicProfile(academicProfile);
        return studentRepository.save(student);
    }

    /* ===================== VALIDATION ===================== */

    private void validateAcademicProfile(AcademicProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Academic profile is required");
        }

        List<AcademicProfileField> fields =
                academicProfileFieldService.findAll();

        Map<String, AcademicProfileField> fieldMap = fields.stream()
                .collect(Collectors.toMap(AcademicProfileField::getName, f -> f));

        Map<String, Object> attributes = profile.getCustomAttributes();

        // Required fields validation
        for (AcademicProfileField field : fields) {
            if (field.isRequired()) {
                if (attributes == null || !attributes.containsKey(field.getName())) {
                    throw new IllegalArgumentException(
                            "Missing required academic field: " + field.getName()
                    );
                }
            }
        }

        // Unknown / type validation
        if (attributes != null) {
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                AcademicProfileField definition = fieldMap.get(entry.getKey());

                if (definition == null) {
                    throw new IllegalArgumentException(
                            "Unknown academic profile field: " + entry.getKey()
                    );
                }

                validateType(entry.getValue(), definition);
            }
        }
    }

    private void validateType(
            Object value,
            AcademicProfileField field
    ) {
        if (value == null) {
            return;
        }

        switch (field.getType()) {
            case TEXT -> {
                if (!(value instanceof String)) {
                    throw new IllegalArgumentException(
                            "Field '" + field.getName() + "' must be TEXT"
                    );
                }
            }
            case NUMBER -> {
                if (!(value instanceof Number)) {
                    throw new IllegalArgumentException(
                            "Field '" + field.getName() + "' must be NUMBER"
                    );
                }
            }
            case BOOLEAN -> {
                if (!(value instanceof Boolean)) {
                    throw new IllegalArgumentException(
                            "Field '" + field.getName() + "' must be BOOLEAN"
                    );
                }
            }
            case DATE -> {
                if (!(value instanceof String)) {
                    throw new IllegalArgumentException(
                            "Field '" + field.getName() + "' must be DATE (ISO String)"
                    );
                }
            }
        }
    }

    private void validateDiplomes(AcademicProfile profile) {
        if (profile.getDiplomes() == null) {
            return;
        }

        for (Diplome d : profile.getDiplomes()) {
            if (d.getNiveauEtude() == null) {
                throw new IllegalArgumentException("Niveau d'étude is required");
            }
            if (d.getYear() == null) {
                throw new IllegalArgumentException("Diploma year is required");
            }
        }
    }

}
