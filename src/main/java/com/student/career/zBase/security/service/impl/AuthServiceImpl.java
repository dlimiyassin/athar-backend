package com.student.career.zBase.security.service.impl;

import com.student.career.exception.ResourceNotFoundException;
import com.student.career.zBase.security.bean.Role;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.bean.enums.UserStatus;
import com.student.career.zBase.security.dao.facade.RoleDao;
import com.student.career.zBase.security.dao.facade.UserDao;
import com.student.career.zBase.security.service.facade.AuthService;
import com.student.career.zBase.security.utils.JWTHelper;
import com.student.career.zBase.security.ws.dto.JWTAuthResponse;
import com.student.career.zBase.security.ws.dto.LoginDto;
import com.student.career.zBase.security.ws.dto.RegisterDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTHelper jwtHelper;

    public AuthServiceImpl(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTHelper jwtHelper) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public JWTAuthResponse login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()
        );
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Email or Password incorrect");
        }
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authenticate.getPrincipal();
        String jwtAccessToken = jwtHelper.generateAccessToken(
                user.getUsername(),
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );
        String jwtRefreshToken = jwtHelper.generateRefreshToken(user.getUsername());
        return new JWTAuthResponse(jwtAccessToken, jwtRefreshToken);
    }

    @Override
    @Transactional
    public String register(RegisterDto registerDto) {

        if (userDao.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        Set<Role> roles = new HashSet<>();
        Role userRole = roleDao.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));
        roles.add(userRole);

        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setEmail(registerDto.getEmail());
        user.setEnabled(true);
        user.setStatus(UserStatus.ACTIF);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(roles);

        userDao.save(user);

        return "User registered successfully";
    }

}
