package com.student.career.zBase.security.ws.transformer;

import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.ws.dto.UserDto;
import com.student.career.zBase.transformer.AbstractTransformer;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;

@Component
public class UserTransformer extends AbstractTransformer<User, UserDto> {

    private final RoleTransformer roleTransformer;

    public UserTransformer(RoleTransformer roleTransformer) {
        this.roleTransformer = roleTransformer;
    }

    @Override
    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        } else {
            User user = new User();
            user.setId(dto.id());
            user.setFirstName(dto.firstName());
            user.setLastName(dto.lastName());
            user.setEmail(dto.email());
            user.setPassword(dto.password());
            user.setEnabled(dto.enabled());
            user.setStatus(dto.status());
            if (dto.lastLogin() != null){
            user.setLastLogin(Instant.parse(dto.lastLogin()));
            }
            user.setPhoneNumber(dto.phoneNumber());
            user.setRoles(new HashSet<>(roleTransformer.toEntity(dto.roleDtos())));
            return user;
        }
    }

    @Override
    public UserDto toDto(User entity) {
        if (entity == null) {
            return null;
        } else {
            UserDto userDto = new UserDto(
                    entity.getId(),
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getEmail(),
                    null,
                    entity.getPhoneNumber(),
                    entity.isEnabled(),
                    entity.getStatus(),
                    entity.getLastLogin()!= null
                            ? entity.getLastLogin().toString()
                            : null,
                    new ArrayList<>(roleTransformer.toDto(entity.getRoles().stream().toList()))
            );
            return userDto;
        }
    }
}
