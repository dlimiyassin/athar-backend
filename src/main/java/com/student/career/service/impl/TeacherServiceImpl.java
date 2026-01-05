package com.student.career.service.impl;

import com.student.career.service.api.TeacherService;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.service.facade.UserService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final UserService userService;

    public TeacherServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<User> findAll() {
        return userService.findAll()
                .stream()
                .filter(user ->
                        user.getRoles() != null &&
                                user.getRoles().stream()
                                        .anyMatch(role -> "ROLE_TEACHER".equals(role.getName()))
                )
                .toList();
    }


    @Override
    public User findById(String id) {
        return userService.findById(id);
    }

    @Override
    public User save(User entity) {
        return userService.save(entity);
    }

    @Override
    public User update(User entity) {
        return userService.update(entity);
    }

    @Override
    public void delete(String id) {
        userService.delete(id);
    }

}
