package com.stock.stocknotification.scheduler;

import com.stock.stocknotification.model.Stock;
import com.stock.stocknotification.service.SmsService;
import com.stock.stocknotification.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StockScheduler {

    private static final Logger logger = LoggerFactory.getLogger(StockScheduler.class);

    private final StockService stockService;
    private final SmsService smsService;

    public StockScheduler(StockService stockService, SmsService smsService) {
        this.stockService = stockService;
        this.smsService = smsService;
    }

    // Run every day 10pm UTC
    // @Scheduled(cron = "0 42 22 * * ?", zone = "UTC")
    @Scheduled(fixedRate = 10000)
    public void sendStockUpdate() {
        logger.info("Scheduler triggered at 10 PM UTC");
        try {
            Stock stock = stockService.getStock("REDINGTON.BSE"); // you can change symbol
            logger.info("Fetched stock data: {}", stock);

            String message = "REDINGTON current price: " + stock.getPrice();
            logger.info("Preparing SMS message: {}", message);

            smsService.sendSms(message);
            logger.info("SMS successfully sent");
        } catch (Exception e) {
            logger.error("Scheduler failed to send stock update: {}", e.getMessage(), e);
        }
    }
}
