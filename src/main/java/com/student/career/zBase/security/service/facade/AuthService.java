package com.student.career.zBase.security.service.facade;


import com.student.career.zBase.security.ws.dto.JWTAuthResponse;
import com.student.career.zBase.security.ws.dto.LoginDto;
import com.student.career.zBase.security.ws.dto.RegisterDto;

public interface AuthService {
    JWTAuthResponse login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
