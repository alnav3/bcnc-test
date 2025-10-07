package com.bcnc.bcnc.web.controller;

import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.domain.port.PriceService;
import com.bcnc.bcnc.web.dto.PriceResponse;
import com.bcnc.bcnc.web.mapper.PriceResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
@Validated
public class PriceController {

    private final PriceService priceService;
    private final PriceResponseMapper priceResponseMapper;

    @GetMapping
    public ResponseEntity<PriceResponse> getApplicablePrice(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam @Positive Long productId,
            @RequestParam @Positive Long brandId) {

        Price price = priceService.findApplicablePrice(date, productId, brandId);
        PriceResponse response = priceResponseMapper.toResponse(price);
        
        return ResponseEntity.ok(response);
    }
}