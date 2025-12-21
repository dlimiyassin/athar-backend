package com.student.career.zBase.security.service.impl;

import com.student.career.exception.ResourceAlreadyExistsException;
import com.student.career.exception.ResourceNotFoundException;
import com.student.career.zBase.criteria.SearchHelper;
import com.student.career.zBase.security.bean.Role;
import com.student.career.zBase.security.dao.criteria.RoleCriteria;
import com.student.career.zBase.security.dao.facade.RoleDao;
import com.student.career.zBase.security.service.facade.RoleService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;
    private final MongoTemplate mongoTemplate;

    public RoleServiceImpl(RoleDao roleDao, MongoTemplate mongoTemplate) {
        this.roleDao = roleDao;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Role createRole(String roleName) {
        return roleDao.save(new Role(roleName));
    }

    @Override
    public Role findByName(String name) {
        return roleDao.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Role", "name", name));
    }

    @Override
    public Role save(Role role) {
        existingRole(role.getName());
        return roleDao.save(role);
    }

    @Override
    public Role update(Role role) {
        findByName(role.getName());
        return roleDao.save(role);
    }

    @Override
    public void delete(String id) {
        findById(id);
        roleDao.deleteById(id);
    }

    @Override
    public Role findById(String id) {
        return roleDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public void existingRole(String roleName) {
        roleDao.findByName(roleName).ifPresent(role -> {
            throw new ResourceAlreadyExistsException("Role", "name", roleName);
        });
    }

    @Override
    public List<Role> findByCriteria(RoleCriteria roleCriteria) {
        List<Role> roles = new ArrayList<>();
        if (roleCriteria != null) {
            Query query = new Query();
            SearchHelper.addCriteria("id", roleCriteria.id(), query);
            SearchHelper.addCriteria("name", roleCriteria.name(), query);
            roles = mongoTemplate.find(query, Role.class);
        }
        return roles;
    }
}
