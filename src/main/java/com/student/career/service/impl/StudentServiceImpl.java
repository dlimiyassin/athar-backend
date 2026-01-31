package com.student.career.service.impl;

import com.student.career.bean.AcademicProfile;
import com.student.career.bean.Student;
import com.student.career.dao.StudentRepository;
import com.student.career.exception.AuthenticationRequiredException;
import com.student.career.exception.ResourceNotFoundException;
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

    public StudentServiceImpl(StudentRepository studentRepository, UserService userService) {
        this.studentRepository = studentRepository;
        this.userService = userService;
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
    public Student update(Student student) {
            if (student.getId() == null) {
                throw new IllegalArgumentException("Student id is required for update");
            }
            return studentRepository.save(student);
    }

    @Override
    public void delete(String studentId) {
            Student studentFound = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));
            userService.delete(studentFound.getUserId());
            studentRepository.deleteById(studentId);
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
            Student student
    ) {
        Student retrievedStudent = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Student not found with id: " + studentId)
                );

        retrievedStudent.setAcademicProfile(student.getAcademicProfile());
        return studentRepository.save(retrievedStudent);
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

        if (profile.getGender() == null || profile.getGender().isBlank()) {
            return true;
        };

        // ---- Current diploma is required ----
        if (profile.getCurrentDiploma() == null) return true;

        var d = profile.getCurrentDiploma();

        if (isBlank(d.getSchool())) return true;
        //if (isBlank(d.getStudyField())) return true;
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
}
