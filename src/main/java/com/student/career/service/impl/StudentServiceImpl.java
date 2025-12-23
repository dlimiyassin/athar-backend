package com.student.career.service.impl;

import com.student.career.bean.AcademicProfile;
import com.student.career.bean.Student;
import com.student.career.dao.StudentRepository;
import com.student.career.service.api.StudentService;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.service.facade.UserService;
import com.student.career.zBase.security.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserService userService;

    public StudentServiceImpl(StudentRepository studentRepository, UserService userService) {
        this.studentRepository = studentRepository;
        this.userService = userService;
    }


    @Override
    public Student save(Student student) {
        student.setUserId(this.userService.loadAuthenticatedUser().getId());
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



    @Override
    public boolean checkProfileSetup() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userService.loadUserByEmail(email);
        Optional<Student> studentOpt = studentRepository.findById(user.getId());

        // ✔ If user exists but student does NOT exist → return false (profile setup is required)
        if (studentOpt.isEmpty()) {
            return false;
        }
        AcademicProfile profile = studentOpt.get().getAcademicProfile();
        if (profile == null) {
            return false;
        }
        return isAcademicProfileFilled(profile);
    }

    private boolean isAcademicProfileFilled(AcademicProfile profile) {

        return profile.getCurrentDiploma() != null
                || (profile.getDiplomes() != null && !profile.getDiplomes().isEmpty())
                || (profile.getCustomAttributes() != null && !profile.getCustomAttributes().isEmpty());
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
