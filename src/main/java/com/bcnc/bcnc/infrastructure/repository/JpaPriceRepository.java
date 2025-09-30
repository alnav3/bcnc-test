package com.bcnc.bcnc.infrastructure.repository;

import com.bcnc.bcnc.infrastructure.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {

    /**
     * Find query to the database to recover the correct
     * price depending of the product, brand and date params.
     * Used a closed range in the logic, meaning that both
     * startDate and endDate are allowed.
     * recover only the 2 highest values with the biggest priority
     *
     * @param productId
     * @param brandId
     * @param date
     */
    @Query("SELECT p FROM PriceEntity p " +
           "WHERE p.productId = :productId " +
           "AND p.brandId = :brandId " +
           "AND p.startDate <= :inputDate " +
           "AND p.endDate >= :inputDate " +
           "ORDER BY p.priority DESC " +
           "LIMIT 2")
    List<PriceEntity> findByProductIdAndBrandIdAndDateRange(
        @Param("productId") Long productId,
        @Param("brandId") Long brandId,
        @Param("inputDate") LocalDateTime date
    );
}
