package com.student.career.ws.controller;


import com.student.career.bean.Student;
import com.student.career.exception.ResourceNotFoundException;
import com.student.career.service.api.StudentProfileService;
import com.student.career.service.api.StudentService;
import com.student.career.ws.dto.AcademicProfileDto;
import com.student.career.ws.dto.StudentDto;
import com.student.career.ws.transformer.StudentTransformer;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.dao.facade.UserDao;
import com.student.career.zBase.security.service.facade.UserService;
import com.student.career.zBase.security.ws.dto.RegisterResponse;
import com.student.career.zBase.security.ws.transformer.UserTransformer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentProfileService studentProfileService;
    private final StudentTransformer studentTransformer;
    private final StudentService studentService;
    private final UserService userService;
    private final UserTransformer userTransformer;

    public StudentController(
            StudentProfileService studentProfileService,
            StudentTransformer studentTransformer, StudentService studentService, UserService userService, UserTransformer userTransformer
    ) {
        this.studentProfileService = studentProfileService;
        this.studentTransformer = studentTransformer;
        this.studentService = studentService;
        this.userService = userService;
        this.userTransformer = userTransformer;
    }

    @GetMapping
    public List<StudentDto> findAll() {
        List<Student> students = studentService.findAll();

        return students.stream()
                .map(student -> {
                    User user = userService.findById(student.getUserId());
                    return studentTransformer.toDto(student, user);
                })
                .toList();
    }

    @GetMapping("/user/{userId}")
    public StudentDto findByUserId(@PathVariable String userId) {
        Student student = studentProfileService.getByUserId(userId);
        User user = userService.findById(userId);
        return studentTransformer.toDto(student, user);
    }

    @PutMapping("/{studentId}/academic-profile")
    public StudentDto updateAcademicProfile(
            @PathVariable String studentId,
            @RequestBody AcademicProfileDto profileDto
    ) {
        Student updated = studentProfileService.updateAcademicProfile(
                studentId,
                studentTransformer
                        .toEntity(new StudentDto(null, null, profileDto))
                        .getAcademicProfile()
        );
        User user = userService.findById(updated.getUserId());
        return studentTransformer.toDto(updated, user);
    }

    @PostMapping
    public ResponseEntity<StudentDto> save(@RequestBody StudentDto studentDto){
        userService.save(userTransformer.toEntity(studentDto.user()));
        Student savedStudent = studentService.save(studentTransformer.toEntity(studentDto));
        StudentDto studentResponse = studentTransformer.toDto(savedStudent);
        return ResponseEntity.ok(studentResponse);
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<StudentDto> completeProfile(@RequestBody StudentDto studentDto){
        Student savedStudent = studentService.save(studentTransformer.toEntity(studentDto));
        StudentDto studentResponse = studentTransformer.toDto(savedStudent);
        return ResponseEntity.ok(studentResponse);
    }

    @GetMapping("/check-student-setup")
    public ResponseEntity<Boolean> checkProfileSetup() {
        boolean isProfileIncompleted = studentService.checkProfileSetup();
        System.out.println(isProfileIncompleted);
        return ResponseEntity.ok(isProfileIncompleted);
    }


}
