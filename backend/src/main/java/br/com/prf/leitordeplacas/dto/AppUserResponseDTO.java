package br.com.prf.leitordeplacas.dto;

import br.com.prf.leitordeplacas.model.AppUser;
import br.com.prf.leitordeplacas.model.enums.Role;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AppUserResponseDTO {

    private Long id;
    private String username;
    private Role role;

    public AppUserResponseDTO(AppUser appuser){
        this.id = appuser.getId();
        this.username = appuser.getUsername();
        this.role = appuser.getRole();
    }

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public Role getRole() {
        return this.role;
    }
}
