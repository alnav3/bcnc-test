package com.bcnc.bcnc.infrastructure.adapter;

import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.domain.port.PriceRepository;
import com.bcnc.bcnc.infrastructure.entity.PriceEntity;
import com.bcnc.bcnc.infrastructure.mapper.PriceEntityMapper;
import com.bcnc.bcnc.infrastructure.repository.JpaPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepository {

    private final JpaPriceRepository jpaPriceRepository;
    private final PriceEntityMapper priceEntityMapper;

    @Override
    public Optional<Price> findApplicablePrice(LocalDateTime date, Long productId, Long brandId) {
        return jpaPriceRepository.findApplicablePrice(brandId, productId, date)
                .map(priceEntityMapper::toDomain);
    }
}