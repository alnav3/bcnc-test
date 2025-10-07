package com.bcnc.bcnc.web.mapper;

import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.web.dto.PriceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PriceResponseMapper {

    PriceResponse toResponse(Price price);
}