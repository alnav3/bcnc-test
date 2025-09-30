package com.bcnc.bcnc.application.service;

/**
 * Custom exception to control cases were the price couldn't
 * be retrieved from the database
 *
 */
public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(String message) {
        super(message);
    }
}
