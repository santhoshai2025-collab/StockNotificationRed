package com.stock.stocknotification.scheduler;

import com.stock.stocknotification.model.Stock;
import com.stock.stocknotification.service.SmsService;
import com.stock.stocknotification.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StockScheduler {

    private static final Logger logger = LoggerFactory.getLogger(StockScheduler.class);

    private final StockService stockService;
    private final SmsService smsService;

    public StockScheduler(StockService stockService, SmsService smsService) {
        this.stockService = stockService;
        this.smsService = smsService;
    }

    //@Scheduled(cron = "0 0 22 * * MON,TUE,WED,THU,FRI", zone = "Asia/Kolkata")
    @Scheduled(fixedRate = 20000) // every 10 seconds
    public void sendStockUpdate() {
        logger.info("Scheduler triggered at 10 PM UTC");
        try {
            Stock stock = stockService.getStock("REDINGTON.BSE");

            // Short date format: Fri 19-Jun
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd-MMM");
            String formattedDate = now.format(formatter);

            // Compact message
            String message = "REDINGTON ₹" + stock.getPrice() + " (" + formattedDate + ")";
            logger.info("Preparing SMS message: {}", message);

            smsService.sendSms(message);
            logger.info("SMS successfully sent");
        } catch (Exception e) {
            logger.error("Scheduler failed to send stock update: {}", e.getMessage(), e);
        }
    }
}
