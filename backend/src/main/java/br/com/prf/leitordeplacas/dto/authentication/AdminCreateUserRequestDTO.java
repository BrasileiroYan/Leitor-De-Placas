package br.com.prf.leitordeplacas.dto.authentication;

import jakarta.validation.constraints.NotBlank;

public class AdminCreateUserRequestDTO {

    @NotBlank
    // Comentário apenas para teste: TIRAR COMENTARIO DEPOIS @EmailPRF
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

