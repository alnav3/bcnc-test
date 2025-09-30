package com.bcnc.bcnc.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PriceControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String getPricesUrl(String applicationDate, String productId, String brandId) {
        return String.format("http://localhost:%d/api/prices?applicationDate=%s&productId=%s&brandId=%s",
                port, applicationDate, productId, brandId);
    }

    @Test
    void test1BasePrice10amOK() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getPricesUrl("2020-06-14T10:00:00", "35455", "1"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode json = objectMapper.readTree(response.getBody());
        assertThat(json.get("productId").asLong()).isEqualTo(35455);
        assertThat(json.get("brandId").asLong()).isEqualTo(1);
        assertThat(json.get("priceList").asLong()).isEqualTo(1);
        assertThat(json.get("startDate").asText()).isEqualTo("2020-06-14T00:00:00");
        assertThat(json.get("endDate").asText()).isEqualTo("2020-12-31T23:59:59");
        assertThat(json.get("price").asDouble()).isEqualTo(35.50);
        assertThat(json.get("currency").asText()).isEqualTo("EUR");
    }

    @Test
    void test2DiscountPrice4pmOK() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getPricesUrl("2020-06-14T16:00:00", "35455", "1"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode json = objectMapper.readTree(response.getBody());
        assertThat(json.get("productId").asLong()).isEqualTo(35455);
        assertThat(json.get("brandId").asLong()).isEqualTo(1);
        assertThat(json.get("priceList").asLong()).isEqualTo(2);
        assertThat(json.get("startDate").asText()).isEqualTo("2020-06-14T15:00:00");
        assertThat(json.get("endDate").asText()).isEqualTo("2020-06-14T18:30:00");
        assertThat(json.get("price").asDouble()).isEqualTo(25.45);
        assertThat(json.get("currency").asText()).isEqualTo("EUR");
    }

    @Test
    void test3BasePriceAfterPromo9pmOK() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getPricesUrl("2020-06-14T21:00:00", "35455", "1"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode json = objectMapper.readTree(response.getBody());
        assertThat(json.get("productId").asLong()).isEqualTo(35455);
        assertThat(json.get("brandId").asLong()).isEqualTo(1);
        assertThat(json.get("priceList").asLong()).isEqualTo(1);
        assertThat(json.get("startDate").asText()).isEqualTo("2020-06-14T00:00:00");
        assertThat(json.get("endDate").asText()).isEqualTo("2020-12-31T23:59:59");
        assertThat(json.get("price").asDouble()).isEqualTo(35.50);
        assertThat(json.get("currency").asText()).isEqualTo("EUR");
    }

    @Test
    void test4MorningSpecialNextDay10amOK() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getPricesUrl("2020-06-15T10:00:00", "35455", "1"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode json = objectMapper.readTree(response.getBody());
        assertThat(json.get("productId").asLong()).isEqualTo(35455);
        assertThat(json.get("brandId").asLong()).isEqualTo(1);
        assertThat(json.get("priceList").asLong()).isEqualTo(3);
        assertThat(json.get("startDate").asText()).isEqualTo("2020-06-15T00:00:00");
        assertThat(json.get("endDate").asText()).isEqualTo("2020-06-15T11:00:00");
        assertThat(json.get("price").asDouble()).isEqualTo(30.50);
        assertThat(json.get("currency").asText()).isEqualTo("EUR");
    }

    @Test
    void test5EveningPriceDay16at9pmOK() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getPricesUrl("2020-06-16T21:00:00", "35455", "1"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode json = objectMapper.readTree(response.getBody());
        assertThat(json.get("productId").asLong()).isEqualTo(35455);
        assertThat(json.get("brandId").asLong()).isEqualTo(1);
        assertThat(json.get("priceList").asLong()).isEqualTo(4);
        assertThat(json.get("startDate").asText()).isEqualTo("2020-06-15T16:00:00");
        assertThat(json.get("endDate").asText()).isEqualTo("2020-12-31T23:59:59");
        assertThat(json.get("price").asDouble()).isEqualTo(38.95);
        assertThat(json.get("currency").asText()).isEqualTo("EUR");
    }

    @Test
    void testPriceNotFound_invalidProduct() {
        // Non-existent product should return 404
        ResponseEntity<String> response = restTemplate.getForEntity(
                getPricesUrl("2020-06-14T10:00:00", "99999", "1"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testPriceNotFound_invalidBrand() {
        // Non-existent brand should return 404
        ResponseEntity<String> response = restTemplate.getForEntity(
                getPricesUrl("2020-06-14T10:00:00", "35455", "999"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testPriceNotFound_dateOutOfRange() {
        // Date outside all price ranges should return 404
        ResponseEntity<String> response = restTemplate.getForEntity(
                getPricesUrl("2019-01-01T10:00:00", "35455", "1"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testBadRequest_missingParameters() {
        // missing params should return 400
        ResponseEntity<String> response = restTemplate.getForEntity(
                String.format("http://localhost:%d/api/prices?applicationDate=2020-06-14T10:00:00", port),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testBadRequest_invalidDateFormat() {
        // date should correctly formatted
        ResponseEntity<String> response = restTemplate.getForEntity(
                getPricesUrl("invalid-date", "35455", "1"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
