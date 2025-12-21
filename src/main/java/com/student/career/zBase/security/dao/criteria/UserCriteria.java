package com.student.career.zBase.security.dao.criteria;

import com.student.career.zBase.security.bean.enums.UserStatus;

import java.util.List;

public record UserCriteria(
        String id,
        String firstName,
        String lastName,
        String email,
        String password,
        Boolean enabled,
        UserStatus status,
        List<RoleCriteria> rolesCriteria
) {
}
