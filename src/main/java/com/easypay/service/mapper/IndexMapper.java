package com.easypay.service.mapper;


import com.easypay.domain.*;
import com.easypay.service.dto.IndexDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Index} and its DTO {@link IndexDTO}.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface IndexMapper extends EntityMapper<IndexDTO, Index> {

    @Mapping(source = "location.id", target = "locationId")
    IndexDTO toDto(Index index);

    @Mapping(source = "locationId", target = "location")
    Index toEntity(IndexDTO indexDTO);

    default Index fromId(Long id) {
        if (id == null) {
            return null;
        }
        Index index = new Index();
        index.setId(id);
        return index;
    }
}
