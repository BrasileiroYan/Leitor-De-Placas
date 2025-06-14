package com.example.plateReader.dto;

import com.example.plateReader.validation.EmailPRF;
import com.example.plateReader.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthRequestDTO {

    @NotBlank(message = "Username required")
    @EmailPRF
    private String username;

    @NotBlank(message = "Password required")
    @ValidPassword
    private String password;

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
