package com.easypay.service.mapper;


import com.easypay.domain.*;
import com.easypay.service.dto.ShopDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Shop} and its DTO {@link ShopDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShopMapper extends EntityMapper<ShopDTO, Shop> {



    default Shop fromId(Long id) {
        if (id == null) {
            return null;
        }
        Shop shop = new Shop();
        shop.setId(id);
        return shop;
    }
}
