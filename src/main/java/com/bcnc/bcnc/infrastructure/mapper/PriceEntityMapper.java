package com.bcnc.bcnc.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.infrastructure.entity.PriceEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PriceEntityMapper {

    @Mapping(source = "brandId", target = "brandId")
    @Mapping(source = "productId", target = "productId")
    Price toDomain(PriceEntity priceEntity);

}
