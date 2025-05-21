package com.example.plateReader.dto;

import com.example.plateReader.model.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class AppUserRequestDTO {

    public AppUserRequestDTO() {
    }

    private String username;
    private String password;
    private AppUser.Role role;

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

    public AppUser.Role getRole() {
        return this.role;
    }
}

