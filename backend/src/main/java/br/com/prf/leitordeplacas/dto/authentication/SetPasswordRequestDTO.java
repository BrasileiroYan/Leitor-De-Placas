package br.com.prf.leitordeplacas.dto.authentication;

import br.com.prf.leitordeplacas.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SetPasswordRequestDTO {

    @NotBlank
    private String token;

    @NotBlank(message = "Password required")
    @ValidPassword
    private String newPassword;

    public SetPasswordRequestDTO(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
