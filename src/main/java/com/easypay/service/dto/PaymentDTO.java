package com.easypay.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.easypay.domain.Payment} entity.
 */
public class PaymentDTO implements Serializable {
    
    private Long id;

    @NotNull
    private Instant day;


    private Long billId;

    private Long shopId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDay() {
        return day;
    }

    public void setDay(Instant day) {
        this.day = day;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        return id != null && id.equals(((PaymentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", day='" + getDay() + "'" +
            ", billId=" + getBillId() +
            ", shopId=" + getShopId() +
            "}";
    }
}
