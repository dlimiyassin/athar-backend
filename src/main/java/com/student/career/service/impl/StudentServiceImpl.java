package com.student.career.service.impl;

import com.student.career.bean.AcademicProfile;
import com.student.career.bean.Student;
import com.student.career.dao.StudentRepository;
import com.student.career.service.api.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findById(String id) {
        return studentRepository.findById(id);
    }

    @Override
    public Object save(Object entity) {
        if (entity instanceof Student student) {
            return studentRepository.save(student);
        }
        throw new IllegalArgumentException("Invalid entity type, expected Student");
    }

    @Override
    public Object update(Object entity) {
        if (entity instanceof Student student) {
            if (student.getId() == null) {
                throw new IllegalArgumentException("Student id is required for update");
            }
            return studentRepository.save(student);
        }
        throw new IllegalArgumentException("Invalid entity type, expected Student");
    }

    @Override
    public void delete(Object entity) {
        if (entity instanceof Student student) {
            studentRepository.delete(student);
            return;
        }
        throw new IllegalArgumentException("Invalid entity type, expected Student");
    }

    /* ================= DOMAIN METHODS ================= */

    @Override
    public Optional<Student> findByUserId(String userId) {
        return studentRepository.findByUserId(userId);
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

        student.setAcademicProfile(academicProfile);
        return studentRepository.save(student);
    }

    /* ================= CRITERIA / EXPORT ================= */

    @Override
    public List findByCriteria(Object criteria) {
        // Not implemented yet
        return List.of();
    }

    @Override
    public void exportToCsv(HttpServletResponse response, List dtos, List fieldsShow) {
        // intentionally ignored
    }

    @Override
    public void exportToExcel(HttpServletResponse response, List dtos, List fieldsShow) {
        // intentionally ignored
    }

    @Override
    public void exportToPdf(HttpServletResponse response, List dtos, List fieldsShow) {
        // intentionally ignored
    }
}
