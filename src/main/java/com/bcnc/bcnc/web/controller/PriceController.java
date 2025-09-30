package com.bcnc.bcnc.web.controller;

import com.bcnc.bcnc.application.service.PriceQueryService;
import com.bcnc.bcnc.domain.model.Price;
import com.bcnc.bcnc.web.dto.PriceResponse;
import com.bcnc.bcnc.web.mapper.PriceResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Price", description = "Price lookup API for product pricing based on date, product, and brand")
public class PriceController {

    private final PriceQueryService priceQueryService;
    private final PriceResponseMapper priceResponseMapper;

    @Operation(
        summary = "Get applicable price",
        description = "Retrieves the applicable price for a product and brand at a specific date and time. Returns the price with the highest priority that is valid for the given parameters and date range."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved price",
            content = @Content(schema = @Schema(implementation = PriceResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No applicable price found for the given parameters",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<PriceResponse> getPrice(
            @Parameter(description = "Application date and time in ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss)", required = true, example = "2020-06-14T10:00:00")
            @RequestParam("applicationDate") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @Parameter(description = "Product identifier", required = true, example = "35455")
            @RequestParam @NotNull Long productId,
            @Parameter(description = "Brand identifier", required = true, example = "1")
            @RequestParam @NotNull Long brandId) {

        Price price = priceQueryService.findApplicablePrice(date, productId, brandId);
        PriceResponse response = priceResponseMapper.toResponse(price);

        return ResponseEntity.ok(response);
    }

}
