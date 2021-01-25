package com.easypay.service.dto;

import lombok.Data;

@Data
public class JwtTokenDTO {

    private String jwtTokenCode;


    public String getJwtTokenCode() {
        return jwtTokenCode;
    }

    public void setJwtTokenCode(String jwtTokenCode) {
        this.jwtTokenCode = jwtTokenCode;
    }
}
