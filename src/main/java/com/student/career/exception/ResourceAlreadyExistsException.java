package com.student.career.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.FOUND)
public class ResourceAlreadyExistsException extends RuntimeException {
    private final String resourceName;
    private final String fieldName;
    private final String fieldValue;

    public ResourceAlreadyExistsException(String resourceName, String fieldName, Long fieldValue) {
        super(String.format("%s already exists with %s : '%s'", resourceName, fieldName, fieldValue)); // Post not found with id : 1
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = String.valueOf(fieldValue);
    }

    public ResourceAlreadyExistsException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s already exists with %s : '%s'", resourceName, fieldName, fieldValue)); // Post not found with id : 1
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceAlreadyExistsException(String resourceName, String fieldName1, String fieldName2, String fieldValue1, String fieldValue2) {
        super(String.format("%s already exists with %s : '%s' and %s : '%s' ", resourceName, fieldName1, fieldValue1, fieldValue1, fieldName2)); // Post not found with id : 1
        this.resourceName = resourceName;
        this.fieldName = fieldName1;
        this.fieldValue = fieldValue1;

    }
}
