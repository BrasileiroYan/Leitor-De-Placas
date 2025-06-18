package com.example.plateReader.dto;

import lombok.Getter;

@Getter
public class AuthResponseDTO {
    private String token;

    private String refreshToken;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token) {
        this.token = token;
    }

    public AuthResponseDTO(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken(){ return refreshToken; }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRefreshToken() { this.refreshToken = refreshToken; }
}
