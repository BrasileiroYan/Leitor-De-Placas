package com.example.plateReader.dto;

import com.example.plateReader.model.AppUser;
import com.example.plateReader.model.enums.Role;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AppUserResponseDTO {

    private Long id;
    private String username;
    private String password;
    private Role role;

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

    public Role getRole() {
        return this.role;
    }
}
