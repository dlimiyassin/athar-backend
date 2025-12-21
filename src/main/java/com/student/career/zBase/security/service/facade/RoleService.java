package com.student.career.zBase.security.service.facade;


import com.student.career.zBase.security.bean.Role;
import com.student.career.zBase.security.dao.criteria.RoleCriteria;

import java.util.List;

public interface RoleService {

    Role createRole(String roleName);

    Role findByName(String name);

    Role save(Role role);

    Role update(Role role);

    void delete(String id);

    Role findById(String id);

    List<Role> findAll();

    void existingRole(String roleName);

    List<Role> findByCriteria(RoleCriteria roleCriteria);


}
