package com.bcnc.bcnc.infrastructure.repository;

import com.bcnc.bcnc.infrastructure.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {

    @Query("SELECT p FROM PriceEntity p " +
           "WHERE p.brand.id = :brandId " +
           "AND p.product.id = :productId " +
           "AND p.startDate <= :applicationDate " +
           "AND p.endDate >= :applicationDate " +
           "ORDER BY p.priority DESC " +
           "LIMIT 1")
    Optional<PriceEntity> findApplicablePrice(
            @Param("brandId") Long brandId,
            @Param("productId") Long productId,
            @Param("applicationDate") LocalDateTime applicationDate
    );
}
