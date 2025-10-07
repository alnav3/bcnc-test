package com.bcnc.bcnc.web.controller;

import com.bcnc.bcnc.application.service.PriceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ControllerExceptionHandlerTest {

    @Autowired
    private ControllerExceptionHandler exceptionHandler;

    @Test
    void contextLoads() {
        assertThat(exceptionHandler).isNotNull();
    }

    @Test
    void priceNotFoundExceptionShouldBeThrowable() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        
        PriceNotFoundException exception = new PriceNotFoundException(date, productId, brandId);
        
        assertThat(exception.getMessage()).contains("No applicable price found");
        assertThat(exception.getMessage()).contains("35455");
        assertThat(exception.getMessage()).contains("1");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}