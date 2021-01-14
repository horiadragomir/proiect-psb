package com.easypay.service.mapper;


import com.easypay.domain.*;
import com.easypay.service.dto.PaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {BillMapper.class, ShopMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

    @Mapping(source = "bill.id", target = "billId")
    @Mapping(source = "shop.id", target = "shopId")
    PaymentDTO toDto(Payment payment);

    @Mapping(source = "billId", target = "bill")
    @Mapping(source = "shopId", target = "shop")
    Payment toEntity(PaymentDTO paymentDTO);

    default Payment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(id);
        return payment;
    }
}
