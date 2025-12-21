package com.student.career.zBase.security.ws.dto;

import lombok.Data;

@Data
public class JWTAuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public JWTAuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
