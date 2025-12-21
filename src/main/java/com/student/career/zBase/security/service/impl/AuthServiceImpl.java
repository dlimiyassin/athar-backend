package com.student.career.zBase.security.service.impl;

import com.student.career.zBase.security.bean.Role;
import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.dao.facade.RoleDao;
import com.student.career.zBase.security.dao.facade.UserDao;
import com.student.career.zBase.security.service.facade.AuthService;
import com.student.career.zBase.security.utils.JWTHelper;
import com.student.career.zBase.security.ws.dto.JWTAuthResponse;
import com.student.career.zBase.security.ws.dto.LoginDto;
import com.student.career.zBase.security.ws.dto.RegisterDto;
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

        if (userDao.findByEmail(registerDto.getEmail()).isEmpty()) {
            Set<Role> roles = new HashSet<>();
            Optional<Role> userRole = roleDao.findByName("ROLE_USER");
            userRole.ifPresent(roles::add);

            User user = new User();
            user.setEmail(registerDto.getEmail());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            user.setRoles(roles);
            userDao.save(user);

            return "User register successfully";
        }
        return "Email already used";
    }
}
