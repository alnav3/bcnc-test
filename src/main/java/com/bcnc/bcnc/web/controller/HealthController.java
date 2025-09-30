package com.bcnc.bcnc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Health", description = "Health check endpoint for monitoring application status")
public class HealthController {

    @Operation(
        summary = "Health check",
        description = "Returns the current health status of the application"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Application is healthy and running",
        content = @Content(
            schema = @Schema(implementation = Map.class),
            examples = @ExampleObject(value = "{\"status\": \"UP\"}")
        )
    )
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
