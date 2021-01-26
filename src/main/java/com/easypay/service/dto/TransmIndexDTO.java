package com.easypay.service.dto;

import javax.validation.constraints.NotNull;

public class TransmIndexDTO {


    @NotNull
    private Long value;

    @NotNull
    private Long month;

    @NotNull
    private Long year;


    private String streetAdress;


    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getStreetAdress() {
        return streetAdress;
    }

    public void setStreetAdress(String streetAdress) {
        this.streetAdress = streetAdress;
    }
}
