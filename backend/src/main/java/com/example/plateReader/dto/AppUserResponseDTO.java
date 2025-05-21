package com.example.plateReader.dto;

import com.example.plateReader.model.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class AppUserResponseDTO {

    private Long id;
    private String username;
    private String password;
    private AppUser.Role role;

    public AppUserResponseDTO(AppUser appuser){
        this.id = appuser.getId();
        this.username = appuser.getUsername();
        this.password = appuser.getPassword();
        this.role = appuser.getRole();
    }

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public AppUser.Role role() {
        return this.role;
    }
}
