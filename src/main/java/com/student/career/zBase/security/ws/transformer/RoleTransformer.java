package com.student.career.zBase.security.ws.transformer;

import com.student.career.zBase.security.bean.Role;
import com.student.career.zBase.security.ws.dto.RoleDto;
import com.student.career.zBase.transformer.AbstractTransformer;
import org.springframework.stereotype.Component;

@Component
public class RoleTransformer extends AbstractTransformer<Role, RoleDto> {

    @Override
    public Role toEntity(RoleDto dto) {
        if (dto == null) {
            return null;
        } else {
            Role role = new Role();
            role.setId(dto.id());
            role.setName(dto.name());
            return role;
        }
    }

    @Override
    public RoleDto toDto(Role entity) {
        if (entity == null) {
            return null;
        } else {
            RoleDto roleDto = new RoleDto(
                    entity.getId(),
                    entity.getName()
            );
            return roleDto;
        }
    }
}
