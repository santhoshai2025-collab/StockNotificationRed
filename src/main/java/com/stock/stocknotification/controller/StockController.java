package com.stock.stocknotification.controller;

import com.stock.stocknotification.model.Stock;
import com.stock.stocknotification.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/stocks/{symbol}")
    public ResponseEntity<?> getStock(@PathVariable String symbol) {
        logger.info("Received request for stock: {}", symbol);
        try {
            Stock stock = stockService.getStock(symbol);
            logger.info("Successfully fetched stock data: {}", stock);
            return ResponseEntity.ok(stock);
        } catch (Exception e) {
            logger.error("Error fetching stock {}: {}", symbol, e.getMessage(), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch stock: " + symbol,
                    e
            );
        }
    }
}
