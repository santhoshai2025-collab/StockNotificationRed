package com.stock.stocknotification.model;

public class Stock {
    private String symbol;
    private double price;

    // Constructor
    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    // Getters
    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }
}

