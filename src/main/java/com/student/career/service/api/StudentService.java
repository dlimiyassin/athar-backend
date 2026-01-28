package com.student.career.service.api;

import com.student.career.bean.AcademicProfile;
import com.student.career.bean.Student;
import com.student.career.zBase.service.IService;

import java.util.List;
import java.util.Optional;

public interface StudentService extends IService {

    Student save(Student student);

    Optional<Student> findById(String id);

    Optional<Student> findByUserId(String userId);

    Optional<Student> findAuthenticatedStudent();

    Student updateAcademicProfile(String studentId, Student student);

    List<Student> findAll();

    boolean checkProfileSetup();

}
