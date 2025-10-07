package com.bcnc.bcnc.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Product {
    Long id;
    String code;
    String name;
    String description;
    String category;
    LocalDateTime createdAt;
}
