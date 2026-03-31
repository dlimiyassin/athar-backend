package com.student.career.service.api;

import com.student.career.bean.AcademicProfile;
import com.student.career.bean.Student;
import com.student.career.zBase.service.IService;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    Student save(Student student);

    Optional<Student> findById(String id);

    Student update(Student student);

    void delete(String studentId);

    Optional<Student> findByUserId(String userId);

    Optional<Student> findAuthenticatedStudent();

    Student updateAcademicProfile(String studentId, Student student);

    List<Student> findAll();

    boolean checkProfileSetup();

}
