package com.example.plateReader.dto;

import lombok.Setter;

public class AuthRequestDTO {

    @Setter
    private String username;
    private String password;

    public AuthRequestDTO() {}

    public AuthRequestDTO(AuthRequestDTO authRequest){
        this.username= authRequest.getUsername();
        this.password= authRequest.getPassword();
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
