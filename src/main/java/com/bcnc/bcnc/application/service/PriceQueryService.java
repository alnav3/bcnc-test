package com.bcnc.bcnc.application.service;

import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.domain.port.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceQueryService {

    private final PriceRepository priceRepository;

    /**
     * find applicable price
     *
     * @param date
     * @param productId
     * @param brandId
     * @return Valid price according to the date, product and brand
     * @throws PriceNotFoundException
     */
    public Price findApplicablePrice(LocalDateTime date, Long productId, Long brandId) {
        List<Price> applicablePrices = priceRepository.findApplicablePrices(date, productId, brandId);

        if (applicablePrices.isEmpty()) {
            throw new PriceNotFoundException(
                String.format("No price found for product %d, brand %d at %s", productId, brandId, date)
            );
        }

        if (applicablePrices.size() == 2 &&
            applicablePrices.get(0).getPriority().equals(applicablePrices.get(1).getPriority())) {
            throw new PriceNotFoundException(
                String.format("Unable to determine applicable price: multiple prices with same priority %d found for product %d, brand %d at %s",
                    applicablePrices.get(0).getPriority(), productId, brandId, date)
            );
        }

        return applicablePrices.get(0);
    }
}
