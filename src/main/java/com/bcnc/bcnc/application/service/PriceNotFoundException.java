package com.bcnc.bcnc.application.service;

import java.time.LocalDateTime;

public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(LocalDateTime date, Long productId, Long brandId) {
        super(String.format("No applicable price found for product %d, brand %d at date %s", 
              productId, brandId, date));
    }
}