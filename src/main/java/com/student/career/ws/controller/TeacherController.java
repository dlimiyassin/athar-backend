package com.student.career.ws.controller;

import com.student.career.service.api.TeacherService;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.ws.dto.UserDto;
import com.student.career.zBase.security.ws.transformer.UserTransformer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {


    private final TeacherService service;
    private final UserTransformer userTransformer;

    public TeacherController(TeacherService service, UserTransformer userTransformer) {
        this.service = service;
        this.userTransformer = userTransformer;
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userDtos = userTransformer.toDto(service.findAll());
        return ResponseEntity.ok(userDtos);
    }


    @PostMapping()
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto)  {
        User user = userTransformer.toEntity(userDto);
        user = service.save(user);
        return new ResponseEntity<>(userTransformer.toDto(user), HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto)  {
        User user = userTransformer.toEntity(userDto);
        user = service.update(user);
        return new ResponseEntity<>(userTransformer.toDto(user), HttpStatus.CREATED);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable String id) {
        UserDto userDto = userTransformer.toDto(service.findById(id));
        return ResponseEntity.ok(userDto);
    }
}
