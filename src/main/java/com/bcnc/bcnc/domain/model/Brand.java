package com.bcnc.bcnc.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Brand {
    Long id;
    String name;
    String description;
    LocalDateTime createdAt;
}
