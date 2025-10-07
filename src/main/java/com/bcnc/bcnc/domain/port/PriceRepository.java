package com.bcnc.bcnc.domain.port;

import com.bcnc.bcnc.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepository {
    Optional<Price> findApplicablePrice(LocalDateTime date, Long productId, Long brandId);
}
