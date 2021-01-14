package com.easypay.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Shop.
 */
@Entity
@Table(name = "shop")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @Column(name = "week_hour_start", nullable = false)
    private Long weekHourStart;

    @NotNull
    @Column(name = "week_hour_end", nullable = false)
    private Long weekHourEnd;

    @NotNull
    @Column(name = "weekend_hour_start", nullable = false)
    private Long weekendHourStart;

    @NotNull
    @Column(name = "weekend_hour_end", nullable = false)
    private Long weekendHourEnd;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public Shop streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Shop phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getWeekHourStart() {
        return weekHourStart;
    }

    public Shop weekHourStart(Long weekHourStart) {
        this.weekHourStart = weekHourStart;
        return this;
    }

    public void setWeekHourStart(Long weekHourStart) {
        this.weekHourStart = weekHourStart;
    }

    public Long getWeekHourEnd() {
        return weekHourEnd;
    }

    public Shop weekHourEnd(Long weekHourEnd) {
        this.weekHourEnd = weekHourEnd;
        return this;
    }

    public void setWeekHourEnd(Long weekHourEnd) {
        this.weekHourEnd = weekHourEnd;
    }

    public Long getWeekendHourStart() {
        return weekendHourStart;
    }

    public Shop weekendHourStart(Long weekendHourStart) {
        this.weekendHourStart = weekendHourStart;
        return this;
    }

    public void setWeekendHourStart(Long weekendHourStart) {
        this.weekendHourStart = weekendHourStart;
    }

    public Long getWeekendHourEnd() {
        return weekendHourEnd;
    }

    public Shop weekendHourEnd(Long weekendHourEnd) {
        this.weekendHourEnd = weekendHourEnd;
        return this;
    }

    public void setWeekendHourEnd(Long weekendHourEnd) {
        this.weekendHourEnd = weekendHourEnd;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shop)) {
            return false;
        }
        return id != null && id.equals(((Shop) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Shop{" +
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
