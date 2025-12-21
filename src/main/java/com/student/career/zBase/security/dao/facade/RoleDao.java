package com.student.career.zBase.security.dao.facade;


import com.student.career.zBase.security.bean.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleDao extends MongoRepository<Role, String> {

    Optional<Role> findByName(String name);
}
