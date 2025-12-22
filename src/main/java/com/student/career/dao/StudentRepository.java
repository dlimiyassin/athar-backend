package com.student.career.dao;

import com.student.career.bean.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {

    Optional<Student> findByUserId(String userId);

    @Query("SELECT s FROM Student s JOIN User u ON s.userId = u.id WHERE u.email = :email")
    Optional<Student> findByUserEmail(@Param("email") String email);
}
