package com.easypay.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

//@Data
public class RegisterDTO {

    @JsonProperty("firstName")
    private  String firstName;

    @JsonProperty("lastname")
    private  String lastname;

    @JsonProperty("phoneNumber")
    private  String phoneNumber;

    @JsonProperty("email")
    private  String email;

    @JsonProperty("password")
    private  String password;

    public RegisterDTO() {}

    public String getFirstName() {
        return firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
