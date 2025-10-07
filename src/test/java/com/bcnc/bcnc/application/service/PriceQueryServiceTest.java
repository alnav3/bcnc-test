package com.bcnc.bcnc.application.service;

import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.domain.port.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceQueryServiceTest {

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private PriceQueryService priceQueryService;

    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDateTime.of(2020, 6, 14, 10, 0);
    }

    @Test
    void findApplicablePrice_shouldReturnPrice_whenPriceExists() {
        Price expectedPrice = Price.builder()
                .id(1L)
                .brandId(1L)
                .productId(35455L)
                .priceList(1L)
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        when(priceRepository.findApplicablePrice(eq(testDate), eq(35455L), eq(1L)))
                .thenReturn(Optional.of(expectedPrice));

        Price result = priceQueryService.findApplicablePrice(testDate, 35455L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getPriceList()).isEqualTo(1L);
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("35.50"));
        assertThat(result.getPriority()).isEqualTo(0);
    }

    @Test
    void findApplicablePrice_shouldThrowException_whenPriceNotFound() {
        when(priceRepository.findApplicablePrice(any(LocalDateTime.class), eq(99999L), eq(1L)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> priceQueryService.findApplicablePrice(testDate, 99999L, 1L))
                .isInstanceOf(PriceNotFoundException.class)
                .hasMessageContaining("No applicable price found");
    }

    @Test
    void findApplicablePrice_shouldReturnCorrectPriceForDifferentScenarios() {
        Price promotionalPrice = Price.builder()
                .id(2L)
                .brandId(1L)
                .productId(35455L)
                .priceList(2L)
                .startDate(LocalDateTime.of(2020, 6, 14, 15, 0))
                .endDate(LocalDateTime.of(2020, 6, 14, 18, 30))
                .priority(1)
                .price(new BigDecimal("25.45"))
                .currency("EUR")
                .build();

        LocalDateTime promotionTime = LocalDateTime.of(2020, 6, 14, 16, 0);
        when(priceRepository.findApplicablePrice(eq(promotionTime), eq(35455L), eq(1L)))
                .thenReturn(Optional.of(promotionalPrice));

        Price result = priceQueryService.findApplicablePrice(promotionTime, 35455L, 1L);

        assertThat(result.getPriceList()).isEqualTo(2L);
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("25.45"));
        assertThat(result.getPriority()).isEqualTo(1);
    }

    @Test
    void findApplicablePrice_shouldHandleDateBoundaries() {
        Price boundaryPrice = Price.builder()
                .id(2L)
                .brandId(1L)
                .productId(35455L)
                .priceList(2L)
                .startDate(LocalDateTime.of(2020, 6, 14, 15, 0))
                .endDate(LocalDateTime.of(2020, 6, 14, 18, 30))
                .priority(1)
                .price(new BigDecimal("25.45"))
                .currency("EUR")
                .build();

        LocalDateTime exactStart = LocalDateTime.of(2020, 6, 14, 15, 0);
        when(priceRepository.findApplicablePrice(eq(exactStart), eq(35455L), eq(1L)))
                .thenReturn(Optional.of(boundaryPrice));

        Price result = priceQueryService.findApplicablePrice(exactStart, 35455L, 1L);

        assertThat(result.getPrice()).isEqualTo(new BigDecimal("25.45"));
    }
}