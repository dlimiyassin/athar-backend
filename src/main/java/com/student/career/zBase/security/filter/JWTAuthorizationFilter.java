package com.student.career.zBase.security.filter;

import com.student.career.zBase.security.utils.JWTHelper;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static com.student.career.zBase.security.constant.JWTUtil.AUTH_HEADER;
import static com.student.career.zBase.security.constant.JWTUtil.SECRET;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JWTHelper jwtHelper;

    public JWTAuthorizationFilter(JWTHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/refresh-token")) {
            filterChain.doFilter(request, response);
        } else {
            String accessToken = jwtHelper.extractTokenFromHeaderIfExists(request.getHeader(AUTH_HEADER));
            if (accessToken != null) {
                try {
                    Algorithm algorithm = Algorithm.HMAC256(SECRET);
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(accessToken);
                    String email = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    for (String role : roles) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            authorities
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (JWTVerificationException e) {
                    // Token verification failed
                    logger.error("JWT Verification failed: {}", e.getCause());
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }

            filterChain.doFilter(request, response);
        }
    }


}
