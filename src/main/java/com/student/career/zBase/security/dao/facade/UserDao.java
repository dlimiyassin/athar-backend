package com.student.career.zBase.security.dao.facade;

import com.student.career.zBase.security.bean.Role;
import com.student.career.zBase.security.bean.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserDao extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findUsersByRoles(Set<Role> roles);

}
