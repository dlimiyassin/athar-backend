package com.student.career.service.api;

import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.service.IService;

import java.util.List;

public interface TeacherService {
    List<User> findAll();

    User findById(String id);

    User save(User entity);

    User update(User entity);

    void delete(String id);
}
