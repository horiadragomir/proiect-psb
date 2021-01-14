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

/**
 * Criteria class for the {@link com.easypay.domain.Shop} entity. This class is used
 * in {@link com.easypay.web.rest.ShopResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shops?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ShopCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter streetAddress;

    private StringFilter phoneNumber;

    private LongFilter weekHourStart;

    private LongFilter weekHourEnd;

    private LongFilter weekendHourStart;

    private LongFilter weekendHourEnd;

    public ShopCriteria() {
    }

    public ShopCriteria(ShopCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.streetAddress = other.streetAddress == null ? null : other.streetAddress.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.weekHourStart = other.weekHourStart == null ? null : other.weekHourStart.copy();
        this.weekHourEnd = other.weekHourEnd == null ? null : other.weekHourEnd.copy();
        this.weekendHourStart = other.weekendHourStart == null ? null : other.weekendHourStart.copy();
        this.weekendHourEnd = other.weekendHourEnd == null ? null : other.weekendHourEnd.copy();
    }

    @Override
    public ShopCriteria copy() {
        return new ShopCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(StringFilter streetAddress) {
        this.streetAddress = streetAddress;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LongFilter getWeekHourStart() {
        return weekHourStart;
    }

    public void setWeekHourStart(LongFilter weekHourStart) {
        this.weekHourStart = weekHourStart;
    }

    public LongFilter getWeekHourEnd() {
        return weekHourEnd;
    }

    public void setWeekHourEnd(LongFilter weekHourEnd) {
        this.weekHourEnd = weekHourEnd;
    }

    public LongFilter getWeekendHourStart() {
        return weekendHourStart;
    }

    public void setWeekendHourStart(LongFilter weekendHourStart) {
        this.weekendHourStart = weekendHourStart;
    }

    public LongFilter getWeekendHourEnd() {
        return weekendHourEnd;
    }

    public void setWeekendHourEnd(LongFilter weekendHourEnd) {
        this.weekendHourEnd = weekendHourEnd;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ShopCriteria that = (ShopCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(weekHourStart, that.weekHourStart) &&
            Objects.equals(weekHourEnd, that.weekHourEnd) &&
            Objects.equals(weekendHourStart, that.weekendHourStart) &&
            Objects.equals(weekendHourEnd, that.weekendHourEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        streetAddress,
        phoneNumber,
        weekHourStart,
        weekHourEnd,
        weekendHourStart,
        weekendHourEnd
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShopCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (streetAddress != null ? "streetAddress=" + streetAddress + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (weekHourStart != null ? "weekHourStart=" + weekHourStart + ", " : "") +
                (weekHourEnd != null ? "weekHourEnd=" + weekHourEnd + ", " : "") +
                (weekendHourStart != null ? "weekendHourStart=" + weekendHourStart + ", " : "") +
                (weekendHourEnd != null ? "weekendHourEnd=" + weekendHourEnd + ", " : "") +
            "}";
    }

}
