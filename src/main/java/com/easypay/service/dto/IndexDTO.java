package com.easypay.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.easypay.domain.Index} entity.
 */
public class IndexDTO implements Serializable {
    
    private Long id;

    @NotNull
    private Long value;

    @NotNull
    private Long month;

    @NotNull
    private Long year;


    private Long locationId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        if (!(o instanceof IndexDTO)) {
            return false;
        }

        return id != null && id.equals(((IndexDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexDTO{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", month=" + getMonth() +
            ", year=" + getYear() +
            ", locationId=" + getLocationId() +
            "}";
    }
}
