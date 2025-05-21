package com.example.plateReader.dto;

import com.example.plateReader.model.AppUser;
import com.example.plateReader.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
public class AppUserRequestDTO {

    private String username;
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
}

