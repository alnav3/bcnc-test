package com.bcnc.bcnc.infrastructure.mapper;

import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.infrastructure.entity.PriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper()
public interface PriceEntityMapper {

    @Mapping(source = "brand.id", target = "brandId")
    @Mapping(source = "product.id", target = "productId")
    Price toDomain(PriceEntity entity);
}
