package com.stock.stocknotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockNotificationApplication.class, args);
    }

}
