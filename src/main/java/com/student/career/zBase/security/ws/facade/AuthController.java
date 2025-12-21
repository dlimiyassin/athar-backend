package com.student.career.zBase.security.ws.facade;

import com.student.career.zBase.security.service.facade.AuthService;
import com.student.career.zBase.security.ws.dto.JWTAuthResponse;
import com.student.career.zBase.security.ws.dto.LoginDto;
import com.student.career.zBase.security.ws.dto.RegisterDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public JWTAuthResponse login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }
}
