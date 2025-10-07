package com.bcnc.bcnc.web.mapper;

import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.web.dto.PriceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PriceResponseMapperTest {

    @Autowired
    private PriceResponseMapper mapper;
    
    private Price domainPrice;

    @BeforeEach
    void setUp() {
        domainPrice = Price.builder()
            .id(1L)
            .brandId(1L)
            .productId(35455L)
            .priceList(2L)
            .startDate(LocalDateTime.of(2020, 6, 14, 15, 0))
            .endDate(LocalDateTime.of(2020, 6, 14, 18, 30))
            .priority(1)
            .price(new BigDecimal("25.45"))
            .currency("EUR")
            .build();
    }

    @Test
    void toResponse_shouldMapAllFieldsCorrectly() {
        PriceResponse response = mapper.toResponse(domainPrice);

        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isEqualTo(35455L);
        assertThat(response.getBrandId()).isEqualTo(1L);
        assertThat(response.getPriceList()).isEqualTo(2L);
        assertThat(response.getStartDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 15, 0));
        assertThat(response.getEndDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 18, 30));
        assertThat(response.getPrice()).isEqualTo(new BigDecimal("25.45"));
        assertThat(response.getCurrency()).isEqualTo("EUR");
    }

    @Test
    void toResponse_shouldHandleNullInput() {
        PriceResponse response = mapper.toResponse(null);

        assertThat(response).isNull();
    }

    @Test
    void toResponse_shouldPreservePrecisionForPrice() {
        Price priceWithPrecision = Price.builder()
            .id(1L)
            .brandId(1L)
            .productId(35455L)
            .priceList(1L)
            .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
            .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
            .priority(0)
            .price(new BigDecimal("123.456"))
            .currency("EUR")
            .build();

        PriceResponse response = mapper.toResponse(priceWithPrecision);

        assertThat(response.getPrice()).isEqualTo(new BigDecimal("123.456"));
        assertThat(response.getPrice().scale()).isEqualTo(3);
    }

    @Test
    void toResponse_shouldHandleMinimumValidData() {
        Price minPrice = Price.builder()
            .id(1L)
            .brandId(1L)
            .productId(1L)
            .priceList(1L)
            .startDate(LocalDateTime.of(2020, 1, 1, 0, 0))
            .endDate(LocalDateTime.of(2020, 1, 1, 23, 59))
            .priority(0)
            .price(new BigDecimal("0.01"))
            .currency("USD")
            .build();

        PriceResponse response = mapper.toResponse(minPrice);

        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isEqualTo(1L);
        assertThat(response.getBrandId()).isEqualTo(1L);
        assertThat(response.getPrice()).isEqualTo(new BigDecimal("0.01"));
        assertThat(response.getCurrency()).isEqualTo("USD");
    }
}