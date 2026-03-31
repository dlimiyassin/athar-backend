package com.student.career.zBase.transformer;

import com.student.career.bean.Student;
import com.student.career.ws.dto.StudentDto;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.util.CollectionUtil;

import java.util.Collections;
import java.util.List;

public abstract class AbstractTransformer<E, D> {

    public abstract E toEntity(D dto);

    public abstract D toDto(E entity);

    public List<E> toEntity(List<D> dtos) {
        if (CollectionUtil.isEmpty(dtos)) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::toEntity).toList();
    }

    public List<D> toDto(List<E> entities) {
        if (CollectionUtil.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).toList();
    }

}
