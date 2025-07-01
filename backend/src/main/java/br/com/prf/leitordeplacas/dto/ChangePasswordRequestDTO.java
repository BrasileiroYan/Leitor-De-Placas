package br.com.prf.leitordeplacas.dto;

import br.com.prf.leitordeplacas.validation.PasswordMatches;
import br.com.prf.leitordeplacas.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@PasswordMatches
@NoArgsConstructor
public class ChangePasswordRequestDTO {

    @NotBlank(message = "Password required")
    @ValidPassword
    private String currentPassword;

    @NotBlank(message = "Password required")
    @ValidPassword
    private String newPassword;

    @NotBlank(message = "Password required")
    @ValidPassword
    private String confirmNewPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
