package com.bcnc.bcnc.domain.port;

import com.bcnc.bcnc.domain.model.Price;

import java.time.LocalDateTime;

public interface PriceService {
    Price findApplicablePrice(LocalDateTime date, Long productId, Long brandId);
}
