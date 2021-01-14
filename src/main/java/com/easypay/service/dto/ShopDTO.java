package com.easypay.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.easypay.domain.Shop} entity.
 */
public class ShopDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String streetAddress;

    @NotNull
    private String phoneNumber;

    @NotNull
    private Long weekHourStart;

    @NotNull
    private Long weekHourEnd;

    @NotNull
    private Long weekendHourStart;

    @NotNull
    private Long weekendHourEnd;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getWeekHourStart() {
        return weekHourStart;
    }

    public void setWeekHourStart(Long weekHourStart) {
        this.weekHourStart = weekHourStart;
    }

    public Long getWeekHourEnd() {
        return weekHourEnd;
    }

    public void setWeekHourEnd(Long weekHourEnd) {
        this.weekHourEnd = weekHourEnd;
    }

    public Long getWeekendHourStart() {
        return weekendHourStart;
    }

    public void setWeekendHourStart(Long weekendHourStart) {
        this.weekendHourStart = weekendHourStart;
    }

    public Long getWeekendHourEnd() {
        return weekendHourEnd;
    }

    public void setWeekendHourEnd(Long weekendHourEnd) {
        this.weekendHourEnd = weekendHourEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopDTO)) {
            return false;
        }

        return id != null && id.equals(((ShopDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShopDTO{" +
            "id=" + getId() +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", weekHourStart=" + getWeekHourStart() +
            ", weekHourEnd=" + getWeekHourEnd() +
            ", weekendHourStart=" + getWeekendHourStart() +
            ", weekendHourEnd=" + getWeekendHourEnd() +
            "}";
    }
}
