package com.example.plateReader.dto;

import com.example.plateReader.model.AppUser;
import com.example.plateReader.model.enums.Role;
import com.example.plateReader.validation.EmailPRF;
import com.example.plateReader.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public class AppUserRequestDTO {

    @NotBlank(message = "Username required")
    @EmailPRF
    private String username;

    @NotBlank(message = "Password required")
    @ValidPassword
    private String password;

    private Role role;

    public AppUserRequestDTO() {
    }

    public AppUserRequestDTO(AppUser appuser){
        this.username = appuser.getUsername();
        this.password = appuser.getPassword();
        this.role = appuser.getRole();
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Role getRole() {
        return this.role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
