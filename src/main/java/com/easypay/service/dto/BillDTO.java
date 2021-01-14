package com.easypay.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.easypay.domain.Bill} entity.
 */
public class BillDTO implements Serializable {
    
    private Long id;

    @NotNull
    private Instant firstDay;

    @NotNull
    private Instant lastDay;

    @NotNull
    private Long value;

    private Boolean paid;


    private Long locationId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean isPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillDTO)) {
            return false;
        }

        return id != null && id.equals(((BillDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillDTO{" +
            "id=" + getId() +
            ", firstDay='" + getFirstDay() + "'" +
            ", lastDay='" + getLastDay() + "'" +
            ", value=" + getValue() +
            ", paid='" + isPaid() + "'" +
            ", locationId=" + getLocationId() +
            "}";
    }
}
