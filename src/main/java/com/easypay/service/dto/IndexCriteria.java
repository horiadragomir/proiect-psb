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
 * Criteria class for the {@link com.easypay.domain.Index} entity. This class is used
 * in {@link com.easypay.web.rest.IndexResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /indices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IndexCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter value;

    private LongFilter month;

    private LongFilter year;

    private LongFilter locationId;

    public IndexCriteria() {
    }

    public IndexCriteria(IndexCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.month = other.month == null ? null : other.month.copy();
        this.year = other.year == null ? null : other.year.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public IndexCriteria copy() {
        return new IndexCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getValue() {
        return value;
    }

    public void setValue(LongFilter value) {
        this.value = value;
    }

    public LongFilter getMonth() {
        return month;
    }

    public void setMonth(LongFilter month) {
        this.month = month;
    }

    public LongFilter getYear() {
        return year;
    }

    public void setYear(LongFilter year) {
        this.year = year;
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
        final IndexCriteria that = (IndexCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(value, that.value) &&
            Objects.equals(month, that.month) &&
            Objects.equals(year, that.year) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        value,
        month,
        year,
        locationId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (value != null ? "value=" + value + ", " : "") +
                (month != null ? "month=" + month + ", " : "") +
                (year != null ? "year=" + year + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
