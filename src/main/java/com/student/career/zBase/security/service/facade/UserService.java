package com.student.career.zBase.security.service.facade;


import com.student.career.zBase.security.bean.Role;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.dao.criteria.UserCriteria;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<User> findUsersByRoles(Set<Role> roles);

    User loadUserByEmail(String email);

    User loadAuthenticatedUser();

    User createUser(String email, String password);

    User save(User user);

    User saveWithAssociatedEmployee(User user);

    User update(User user);

    User updatedWithAssociatedEmployee(User user);

    void delete(String id);

    User findById(String id);

    List<User> findAll();

    void assignRoleToUser(String email, String roleName);

    User updatePassword(String email, String newPassword);

    List<User> findByCriteria(UserCriteria userCriteria);

    User updatePasswordBasedOnCurrentPassword(String email, String currentPassword, String newPassword);
}
