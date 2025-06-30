package com.example.plateReader.dto.authentication;

import com.example.plateReader.validation.ValidPassword;
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
