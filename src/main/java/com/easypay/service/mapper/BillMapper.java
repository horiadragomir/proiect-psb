package com.easypay.service.mapper;


import com.easypay.domain.*;
import com.easypay.service.dto.BillDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bill} and its DTO {@link BillDTO}.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface BillMapper extends EntityMapper<BillDTO, Bill> {

    @Mapping(source = "location.id", target = "locationId")
    BillDTO toDto(Bill bill);

    @Mapping(source = "locationId", target = "location")
    Bill toEntity(BillDTO billDTO);

    default Bill fromId(Long id) {
        if (id == null) {
            return null;
        }
        Bill bill = new Bill();
        bill.setId(id);
        return bill;
    }
}
