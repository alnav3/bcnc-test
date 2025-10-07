package com.bcnc.bcnc.infrastructure.mapper;

import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.infrastructure.entity.BrandEntity;
import com.bcnc.bcnc.infrastructure.entity.PriceEntity;
import com.bcnc.bcnc.infrastructure.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PriceEntityMapperTest {

    @Autowired
    private PriceEntityMapper mapper;
    
    private PriceEntity priceEntity;
    private BrandEntity brandEntity;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        brandEntity = new BrandEntity();
        brandEntity.setId(1L);
        brandEntity.setName("ZARA");

        productEntity = new ProductEntity();
        productEntity.setId(35455L);
        productEntity.setName("Test Product");

        priceEntity = new PriceEntity();
        priceEntity.setId(1L);
        priceEntity.setBrand(brandEntity);
        priceEntity.setBrandId(1L);
        priceEntity.setProduct(productEntity);
        priceEntity.setProductId(35455L);
        priceEntity.setPriceList(1L);
        priceEntity.setStartDate(LocalDateTime.of(2020, 6, 14, 0, 0));
        priceEntity.setEndDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        priceEntity.setPriority(0);
        priceEntity.setPrice(new BigDecimal("35.50"));
        priceEntity.setCurrency("EUR");
    }

    @Test
    void toDomain_shouldMapAllFieldsCorrectly() {
        Price domainPrice = mapper.toDomain(priceEntity);

        assertThat(domainPrice).isNotNull();
        assertThat(domainPrice.getId()).isEqualTo(1L);
        assertThat(domainPrice.getPriceList()).isEqualTo(1L);
        assertThat(domainPrice.getStartDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 0, 0));
        assertThat(domainPrice.getEndDate()).isEqualTo(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        assertThat(domainPrice.getPriority()).isEqualTo(0);
        assertThat(domainPrice.getPrice()).isEqualTo(new BigDecimal("35.50"));
        assertThat(domainPrice.getCurrency()).isEqualTo("EUR");
    }

    @Test
    void toDomain_shouldMapBrandIdCorrectly() {
        Price domainPrice = mapper.toDomain(priceEntity);

        assertThat(domainPrice.getBrandId()).isEqualTo(1L);
    }

    @Test
    void toDomain_shouldMapProductIdCorrectly() {
        Price domainPrice = mapper.toDomain(priceEntity);

        assertThat(domainPrice.getProductId()).isEqualTo(35455L);
    }

    @Test
    void toDomain_shouldHandleNullInput() {
        Price domainPrice = mapper.toDomain(null);

        assertThat(domainPrice).isNull();
    }

    @Test
    void toDomain_shouldHandleNullBrandAndProduct() {
        priceEntity.setBrand(null);
        priceEntity.setBrandId(null);
        priceEntity.setProduct(null);
        priceEntity.setProductId(null);

        Price domainPrice = mapper.toDomain(priceEntity);

        assertThat(domainPrice).isNotNull();
        assertThat(domainPrice.getBrandId()).isNull();
        assertThat(domainPrice.getProductId()).isNull();
    }

    @Test
    void toDomain_shouldPreservePrecisionForPrice() {
        priceEntity.setPrice(new BigDecimal("123.456"));

        Price domainPrice = mapper.toDomain(priceEntity);

        assertThat(domainPrice.getPrice()).isEqualTo(new BigDecimal("123.456"));
        assertThat(domainPrice.getPrice().scale()).isEqualTo(3);
    }
}