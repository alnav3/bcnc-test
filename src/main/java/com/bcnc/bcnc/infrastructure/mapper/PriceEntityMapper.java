package com.bcnc.bcnc.infrastructure.mapper;

import com.bcnc.bcnc.domain.model.Brand;
import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.domain.model.Product;
import com.bcnc.bcnc.infrastructure.entity.BrandEntity;
import com.bcnc.bcnc.infrastructure.entity.PriceEntity;
import com.bcnc.bcnc.infrastructure.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PriceEntityMapper {

    @Mapping(source = "brand.id", target = "brandId")
    @Mapping(source = "product.id", target = "productId")
    Price toDomain(PriceEntity priceEntity);

    @Mapping(source = "brandId", target = "brand.id")
    @Mapping(source = "brandId", target = "brandId")
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "productId", target = "productId")
    PriceEntity toEntity(Price price);

    Brand brandToDomain(BrandEntity brandEntity);

    BrandEntity brandToEntity(Brand brand);

    @Mapping(source = "name", target = "name")
    Product productToDomain(ProductEntity productEntity);

    @Mapping(source = "name", target = "name")
    ProductEntity productToEntity(Product product);
}