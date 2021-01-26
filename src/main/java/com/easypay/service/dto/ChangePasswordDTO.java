package com.easypay.service.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {

    public ChangePasswordDTO() {

    }

    private String newPassword;

    private String confirmPassword;


    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
