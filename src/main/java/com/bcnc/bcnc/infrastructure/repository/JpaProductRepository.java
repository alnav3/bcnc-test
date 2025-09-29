package com.bcnc.bcnc.infrastructure.repository;

import com.bcnc.bcnc.infrastructure.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> { }
