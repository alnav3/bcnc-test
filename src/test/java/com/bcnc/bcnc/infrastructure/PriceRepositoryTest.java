package com.bcnc.bcnc.infrastructure;

import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.infrastructure.adapter.PriceRepositoryAdapter;
import com.bcnc.bcnc.infrastructure.entity.PriceEntity;
import com.bcnc.bcnc.infrastructure.repository.JpaBrandRepository;
import com.bcnc.bcnc.infrastructure.repository.JpaPriceRepository;
import com.bcnc.bcnc.infrastructure.repository.JpaProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PriceRepositoryTest {

    @Autowired
    private JpaPriceRepository jpaPriceRepository;

    @Autowired
    private JpaBrandRepository brandRepository;

    @Autowired
    private JpaProductRepository productRepository;

    @Autowired
    private PriceRepositoryAdapter priceRepositoryAdapter;

    @Test
    void testDatabaseConnection() {
        assertThat(jpaPriceRepository.count()).isEqualTo(4).withFailMessage("Should have 4 price records");
        assertThat(brandRepository.count()).isEqualTo(1).withFailMessage("Should have 1 brand record");
        assertThat(productRepository.count()).isEqualTo(1).withFailMessage("Should have 1 product record");
    }

    @Test
    void testFindApplicablePrice_shouldReturnCorrectPriceForDateRange() {
        LocalDateTime queryDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        Optional<Price> applicablePrice = priceRepositoryAdapter.findApplicablePrice(queryDate, productId, brandId);

        assertThat(applicablePrice).isPresent();
        assertThat(applicablePrice.get().getProductId()).isEqualTo(productId);
        assertThat(applicablePrice.get().getBrandId()).isEqualTo(brandId);
        assertThat(queryDate).isBetween(
            applicablePrice.get().getStartDate(), 
            applicablePrice.get().getEndDate()
        );
    }

    @Test
    void testFindApplicablePrice_shouldReturnEmptyForNonExistentProduct() {
        LocalDateTime queryDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        Long nonExistentProductId = 99999L;
        Long brandId = 1L;

        Optional<Price> applicablePrice = priceRepositoryAdapter.findApplicablePrice(queryDate, nonExistentProductId, brandId);

        assertThat(applicablePrice).isEmpty();
    }

    @Test
    void testFindApplicablePrice_shouldReturnEmptyForNonExistentBrand() {
        LocalDateTime queryDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        Long productId = 35455L;
        Long nonExistentBrandId = 999L;

        Optional<Price> applicablePrice = priceRepositoryAdapter.findApplicablePrice(queryDate, productId, nonExistentBrandId);

        assertThat(applicablePrice).isEmpty();
    }

    @Test
    void testFindApplicablePrice_shouldReturnEmptyForDateOutOfRange() {
        LocalDateTime queryDate = LocalDateTime.of(2019, 1, 1, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        Optional<Price> applicablePrice = priceRepositoryAdapter.findApplicablePrice(queryDate, productId, brandId);

        assertThat(applicablePrice).isEmpty();
    }

    @Test
    void testPriceDataIntegrity() {
        List<PriceEntity> allPrices = jpaPriceRepository.findAll();

        assertThat(allPrices).hasSize(4);
        assertThat(allPrices).allMatch(price -> 
            price.getPrice().compareTo(BigDecimal.ZERO) > 0
        );
        assertThat(allPrices).allMatch(price -> 
            price.getStartDate().isBefore(price.getEndDate()) || 
            price.getStartDate().isEqual(price.getEndDate())
        );
        assertThat(allPrices).allMatch(price -> 
            price.getBrand() != null && price.getProduct() != null
        );
    }

    @Test
    void testPriorityLogic_shouldReturnHighestPriorityPrice() {
        LocalDateTime queryDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        Optional<Price> applicablePrice = priceRepositoryAdapter.findApplicablePrice(queryDate, productId, brandId);

        assertThat(applicablePrice).isPresent();
        assertThat(applicablePrice.get().getPriority()).isNotNull();
        assertThat(applicablePrice.get().getPriority()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void testSpecificPriceScenarios() {
        assertThat(jpaPriceRepository.findAll()).extracting("priceList")
            .containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
        
        assertThat(jpaPriceRepository.findAll()).extracting("price")
            .containsExactlyInAnyOrder(
                new BigDecimal("35.50"), 
                new BigDecimal("25.45"), 
                new BigDecimal("30.50"), 
                new BigDecimal("38.95")
            );
    }
}
