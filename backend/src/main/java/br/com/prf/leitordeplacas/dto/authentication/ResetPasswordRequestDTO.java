package br.com.prf.leitordeplacas.dto.authentication;

import br.com.prf.leitordeplacas.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public class ResetPasswordRequestDTO {

    @NotBlank(message = "Token required")
    private String resetToken;

    @NotBlank(message = "Password required")
    @ValidPassword
    private String newPassword;

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
