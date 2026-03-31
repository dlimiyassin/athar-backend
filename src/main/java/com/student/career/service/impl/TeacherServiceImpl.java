package com.student.career.service.impl;

import com.student.career.service.api.TeacherService;
import com.student.career.zBase.security.bean.Role;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.service.facade.RoleService;
import com.student.career.zBase.security.service.facade.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final UserService userService;
    private final RoleService roleService;
    public TeacherServiceImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
        Role techerRole = roleService.findByName("ROLE_TEACHER");
        if (techerRole != null){
            Set<Role> roles = new HashSet<>();
            roles.add(techerRole);
            entity.setRoles(roles);
        }
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
