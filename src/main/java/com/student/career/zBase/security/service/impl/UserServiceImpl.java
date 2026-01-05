package com.student.career.zBase.security.service.impl;


import com.student.career.exception.AuthenticationRequiredException;
import com.student.career.exception.GlobalException;
import com.student.career.exception.ResourceAlreadyExistsException;
import com.student.career.exception.ResourceNotFoundException;
import com.student.career.zBase.criteria.SearchHelper;
import com.student.career.zBase.security.bean.Role;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.bean.enums.UserStatus;
import com.student.career.zBase.security.dao.criteria.RoleCriteria;
import com.student.career.zBase.security.dao.criteria.UserCriteria;
import com.student.career.zBase.security.dao.facade.UserDao;
import com.student.career.zBase.security.service.facade.RoleService;
import com.student.career.zBase.security.service.facade.UserService;
import com.student.career.zBase.security.utils.SecurityUtil;
import com.student.career.zBase.util.CollectionUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final MongoTemplate mongoTemplate;

    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder, MongoTemplate mongoTemplate) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<User> findUsersByRoles(Set<Role> roles) {
        Set<Role> roleSet = new HashSet<>();
        for (Role role : roles) {
            Optional<Role> userRole = Optional.ofNullable(roleService.findByName(role.getName()));
            userRole.ifPresent(roleSet::add);
        }
        return userDao.findUsersByRoles(roleSet);
    }

    @Override
    public User loadUserByEmail(String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Email", email)
                );
    }

    @Override
    public User loadAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationRequiredException("No authenticated user found");
        }

        String email = authentication.getName();
        return userDao.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Email", email)
                );
    }


    @Override
    public User createUser(String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        return userDao.save(new User(email, encodedPassword));
    }

    @Override
    public User save(User user) {
        findByEmail(user.getEmail());
        prepareSave(user);
        return userDao.save(user);
    }

    private void prepareSave(User user) {
        Set<Role> roles = new HashSet<>();
        if (CollectionUtil.isNotEmpty(user.getRoles())) {
            for (Role role : user.getRoles()) {
                Role foundedRole = roleService.findByName(role.getName());
                roles.add(foundedRole);
            }
            user.setRoles(roles);
        }
    }

    @Override
    @Transactional
    public User saveWithAssociatedEmployee(User user) {
        findByEmail(user.getEmail());
        String password = SecurityUtil.generatePassword();
        user.setPassword(passwordEncoder.encode(password));
        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            Optional<Role> userRole = Optional.ofNullable(roleService.findByName(role.getName()));
            userRole.ifPresent(roles::add);
        }
        user.setRoles(roles);

        return userDao.save(user);
    }

    @Override
    @Transactional
    public User updatedWithAssociatedEmployee(User user) {
        User foundedUser = findById(user.getId());
        user.setPassword(foundedUser.getPassword());
        return userDao.save(user);
    }

    @Override
    public User update(User user) {
        User found = findById(user.getId());

        found.setFirstName(user.getFirstName());
        found.setLastName(user.getLastName());
        found.setPhoneNumber(user.getPhoneNumber());
        found.setEnabled(user.isEnabled());

        if (found.isEnabled()) {
            found.setStatus(UserStatus.ACTIF);
        } else {
            found.setStatus(UserStatus.BLOQUE);
        }

        return userDao.save(found);
    }


    @Override
    public void delete(String id) {
        findById(id);
        userDao.deleteById(id);
    }

    @Override
    public User findById(String id) {
        return userDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    private void findByEmail(String email) {
        userDao.findByEmail(email).ifPresent(user -> {
            throw new ResourceAlreadyExistsException("User", "email", email);
        });
    }

    @Override
    public void assignRoleToUser(String email, String roleName) {
        User user = loadUserByEmail(email);
        Role role = roleService.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public User updatePassword(String email, String newPassword) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));
        user.setPassword(passwordEncoder.encode(newPassword));
        return userDao.save(user);
    }

    @Override
    public List<User> findByCriteria(UserCriteria userCriteria) {
        List<User> users = new ArrayList<>();
        if (userCriteria != null) {
            Query query = new Query();
            SearchHelper.addCriteria("id", userCriteria.id(), query);
            SearchHelper.addCriteria("email", userCriteria.email(), query);
            SearchHelper.addCriteria("firstName", userCriteria.firstName(), query);
            SearchHelper.addCriteria("lastName", userCriteria.lastName(), query);
            SearchHelper.addCriteria("enabled", userCriteria.enabled(), query);
            SearchHelper.addCriteria("userStatus", userCriteria.status(), query);
            SearchHelper.addCriteria("roles.id", userCriteria.rolesCriteria(), RoleCriteria::id, query);

            users = mongoTemplate.find(query, User.class);
        }
        return users;
    }

    @Override
    public User updatePasswordBasedOnCurrentPassword(String email, String currentPassword, String newPassword) {
        User foundedUser = userDao.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));
        if (passwordEncoder.matches(currentPassword, foundedUser.getPassword())) {
            foundedUser.setPassword(passwordEncoder.encode(newPassword));
            return userDao.save(foundedUser);
        } else {
            throw new GlobalException(HttpStatus.CONFLICT, "Current password is incorrect");
        }
    }

}
