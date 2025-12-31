package com.student.career.service.impl;

import com.student.career.bean.AcademicProfile;
import com.student.career.bean.Student;
import com.student.career.dao.StudentRepository;
import com.student.career.exception.AuthenticationRequiredException;
import com.student.career.service.api.AcademicProfileFieldService;
import com.student.career.service.api.StudentService;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.service.facade.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserService userService;
    private final AcademicProfileFieldService academicProfileFieldService;

    public StudentServiceImpl(StudentRepository studentRepository, UserService userService, AcademicProfileFieldService academicProfileFieldService) {
        this.studentRepository = studentRepository;
        this.userService = userService;
        this.academicProfileFieldService = academicProfileFieldService;
    }


    @Override
    public Student save(Student student) {
        try {
            User currentUser = userService.loadAuthenticatedUser();
            student.setUserId(currentUser.getId());
        } catch (AuthenticationRequiredException ignored) {}

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
        return studentRepository.findFirstByUserId(userId);
    }

    @Override
    public Optional<Student> findAuthenticatedStudent() {
        User user = userService.loadAuthenticatedUser();
        return findByUserId(user.getId());
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

        try {
            User user = userService.loadAuthenticatedUser();
            Optional<Student> studentOpt = studentRepository.findFirstByUserId(user.getId());

            if (studentOpt.isEmpty()) {
                return true; // profile is incomplete
            }

            AcademicProfile profile = studentOpt.get().getAcademicProfile();
            return isAcademicProfileIncomplete(profile);

        } catch (AuthenticationRequiredException ignored) {}

        return true; // default: incomplete
    }

    private boolean isAcademicProfileIncomplete(AcademicProfile profile) {

        if (profile == null) return true;

        // ---- Current diploma is required ----
        if (profile.getCurrentDiploma() == null) return true;

        var d = profile.getCurrentDiploma();

        if (isBlank(d.getSchool())) return true;
        if (isBlank(d.getStudyField())) return true;
        if (isBlank(d.getTitle())) return true;

        // ---- Diplomas list required ----
        if (profile.getDiplomas() == null || profile.getDiplomas().isEmpty()) {
            return true;
        }

        // ---- Custom attributes required ----
        return profile.getCustomAttributes() == null;
    }


    private boolean isBlank(String s) {
        return s == null || s.isBlank();
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
