package com.bcnc.bcnc.application.service;

import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.domain.port.PriceRepository;
import com.bcnc.bcnc.domain.port.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PriceQueryService implements PriceService {

    private final PriceRepository priceRepository;

    @Override
    public Price findApplicablePrice(LocalDateTime date, Long productId, Long brandId) {
        return priceRepository.findApplicablePrice(date, productId, brandId)
                .orElseThrow(() -> new PriceNotFoundException(date, productId, brandId));
    }
}