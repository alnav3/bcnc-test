package com.bcnc.bcnc.infrastructure.adapter;

import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.domain.port.PriceRepository;
import com.bcnc.bcnc.infrastructure.entity.PriceEntity;
import com.bcnc.bcnc.infrastructure.mapper.PriceEntityMapper;
import com.bcnc.bcnc.infrastructure.repository.JpaPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepository {

    private final JpaPriceRepository jpaPriceRepository;
    private final PriceEntityMapper priceEntityMapper;

    @Override
    public List<Price> findApplicablePrices(LocalDateTime date, Long productId, Long brandId) {
        List<PriceEntity> entities = jpaPriceRepository.findByProductIdAndBrandIdAndDateRange(
            productId,
            brandId,
            date
        );

        return entities.stream()
                .map(priceEntityMapper::toDomain)
                .toList();
    }
}
