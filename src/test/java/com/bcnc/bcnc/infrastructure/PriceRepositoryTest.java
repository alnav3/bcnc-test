package com.bcnc.bcnc.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bcnc.bcnc.infrastructure.repository.JpaBrandRepository;
import com.bcnc.bcnc.infrastructure.repository.JpaPriceRepository;
import com.bcnc.bcnc.infrastructure.repository.JpaProductRepository;

@SpringBootTest
class PriceRepositoryTest {


    @Autowired
    private JpaPriceRepository jpaPriceRepository;

    @Autowired
    private JpaBrandRepository brandRepository;

    @Autowired
    private JpaProductRepository productRepository;

    @Test
    void testDatabaseConnection() {
        assertEquals(4, jpaPriceRepository.count(), "Should have 4 price records");
        assertEquals(1, brandRepository.count(), "Should have 1 brand record");
        assertEquals(1, productRepository.count(), "Should have 1 product record");
    }

}
