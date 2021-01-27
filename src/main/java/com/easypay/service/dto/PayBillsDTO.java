package com.easypay.service.dto;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public class PayBillsDTO {

    @NotNull
    private String streetAddress;

    @NotNull
    private String postalCode;

    @NotNull
    private String city;

    @NotNull
    private Instant firstDay;

    @NotNull
    private Instant lastDay;

    @NotNull
    private Long value;


    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Instant getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(Instant firstDay) {
        this.firstDay = firstDay;
    }

    public Instant getLastDay() {
        return lastDay;
    }

    public void setLastDay(Instant lastDay) {
        this.lastDay = lastDay;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
