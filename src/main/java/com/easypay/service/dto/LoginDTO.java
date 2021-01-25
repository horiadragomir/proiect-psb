package com.easypay.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDTO {


    @JsonProperty("email")
    private  String email;

    @JsonProperty("password")
    private  String password;

    public LoginDTO() {}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
