package com.stock.stocknotification.service;

import com.stock.stocknotification.model.Stock;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    @Value("${alphavantage.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Stock getStock(String symbol) {
        logger.info("Fetching stock for symbol: {}", symbol);

        try {
            // Build API URL
            String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
                    + symbol + "&apikey=" + apiKey;
            logger.debug("Calling Alpha Vantage API: {}", url);

            // Call API
            String response = restTemplate.getForObject(url, String.class);
            logger.debug("Raw API response: {}", response);

            // Parse JSON
            JSONObject json = new JSONObject(response);

            if (!json.has("Global Quote")) {
                logger.error("No 'Global Quote' found in response for symbol: {}", symbol);
                throw new RuntimeException("Invalid API response: " + response);
            }

            JSONObject quote = json.getJSONObject("Global Quote");

            String stockSymbol = quote.optString("01. symbol", symbol);
            double price = Double.parseDouble(quote.optString("05. price", "0.0"));

            Stock stock = new Stock(stockSymbol, price);
            logger.info("Parsed stock data: {}", stock);

            return stock;
        } catch (Exception e) {
            logger.error("Error fetching stock {}: {}", symbol, e.getMessage(), e);
            throw e; // rethrow so controller can handle it
        }
    }
}
