package com.easypay.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.easypay.domain.Bill} entity. This class is used
 * in {@link com.easypay.web.rest.BillResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bills?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BillCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter firstDay;

    private InstantFilter lastDay;

    private LongFilter value;

    private BooleanFilter paid;

    private LongFilter locationId;

    public BillCriteria() {
    }

    public BillCriteria(BillCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstDay = other.firstDay == null ? null : other.firstDay.copy();
        this.lastDay = other.lastDay == null ? null : other.lastDay.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.paid = other.paid == null ? null : other.paid.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public BillCriteria copy() {
        return new BillCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(InstantFilter firstDay) {
        this.firstDay = firstDay;
    }

    public InstantFilter getLastDay() {
        return lastDay;
    }

    public void setLastDay(InstantFilter lastDay) {
        this.lastDay = lastDay;
    }

    public LongFilter getValue() {
        return value;
    }

    public void setValue(LongFilter value) {
        this.value = value;
    }

    public BooleanFilter getPaid() {
        return paid;
    }

    public void setPaid(BooleanFilter paid) {
        this.paid = paid;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BillCriteria that = (BillCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstDay, that.firstDay) &&
            Objects.equals(lastDay, that.lastDay) &&
            Objects.equals(value, that.value) &&
            Objects.equals(paid, that.paid) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstDay,
        lastDay,
        value,
        paid,
        locationId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstDay != null ? "firstDay=" + firstDay + ", " : "") +
                (lastDay != null ? "lastDay=" + lastDay + ", " : "") +
                (value != null ? "value=" + value + ", " : "") +
                (paid != null ? "paid=" + paid + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
