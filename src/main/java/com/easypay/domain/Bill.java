package com.easypay.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Bill.
 */
@Entity
@Table(name = "bill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "first_day", nullable = false)
    private Instant firstDay;

    @NotNull
    @Column(name = "last_day", nullable = false)
    private Instant lastDay;

    @NotNull
    @Column(name = "value", nullable = false)
    private Long value;

    @Column(name = "paid")
    private Boolean paid;

    @JsonIgnore
    @ManyToOne
    @JsonIgnoreProperties(value = "bills", allowSetters = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFirstDay() {
        return firstDay;
    }

    public Bill firstDay(Instant firstDay) {
        this.firstDay = firstDay;
        return this;
    }

    public void setFirstDay(Instant firstDay) {
        this.firstDay = firstDay;
    }

    public Instant getLastDay() {
        return lastDay;
    }

    public Bill lastDay(Instant lastDay) {
        this.lastDay = lastDay;
        return this;
    }

    public void setLastDay(Instant lastDay) {
        this.lastDay = lastDay;
    }

    public Long getValue() {
        return value;
    }

    public Bill value(Long value) {
        this.value = value;
        return this;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Boolean isPaid() {
        return paid;
    }

    public Bill paid(Boolean paid) {
        this.paid = paid;
        return this;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Location getLocation() {
        return location;
    }

    public Bill location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bill)) {
            return false;
        }
        return id != null && id.equals(((Bill) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bill{" +
            "id=" + getId() +
            ", firstDay='" + getFirstDay() + "'" +
            ", lastDay='" + getLastDay() + "'" +
            ", value=" + getValue() +
            ", paid='" + isPaid() + "'" +
            "}";
    }
}
