package com.student.career.zBase.security.ws.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String name;
    private String email;
    private String password;
    private String numTel;
}
