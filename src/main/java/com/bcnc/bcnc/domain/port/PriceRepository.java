package com.bcnc.bcnc.domain.port;

import com.bcnc.bcnc.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {
    /**
     * Find applicable prices for the following params:
     *
     * @param date
     * @param productId
     * @param brandId
     * @return
     */
    List<Price> findApplicablePrices(LocalDateTime date, Long productId, Long brandId);
}
