package com.bcnc.bcnc.web.controller;

import com.bcnc.bcnc.application.service.PriceQueryService;
import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.web.dto.PriceResponse;
import com.bcnc.bcnc.web.mapper.PriceResponseMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
@Validated
public class PriceController {

    private final PriceQueryService priceQueryService;
    private final PriceResponseMapper priceResponseMapper;

    /**
     * retrieves the applicable price for a product and brand at a specific date and time.
     * returns the price with the highest priority (higher numerical value = higher priority) 
     * that is valid for the given parameters and date range.
     *
     * @param date the application date and time (ISO 8601 format: yyyy-MM-ddTHH:mm:ss)
     * @param productId the product identifier
     * @param brandId the brand identifier
     * @return ResponseEntity containing the applicable price information
     */
    @GetMapping
    public ResponseEntity<PriceResponse> getPrice(
            @RequestParam("applicationDate") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam @NotNull Long productId,
            @RequestParam @NotNull Long brandId) {

        Price price = priceQueryService.findApplicablePrice(date, productId, brandId);
        PriceResponse response = priceResponseMapper.toResponse(price);

        return ResponseEntity.ok(response);
    }

}
