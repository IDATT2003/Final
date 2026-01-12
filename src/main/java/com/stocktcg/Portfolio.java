package com.stocktcg;

import java.util.HashMap;
import java.util.Map;

/**
 * Global portfolio manager - tracks cash and share holdings per stock
 */
public class Portfolio {
    private double cash;
    private Map<String, Integer> shares; // stock symbol -> share count
    private Map<String, Double> latestPrices; // stock symbol -> latest price
    
    public Portfolio(double initialCash) {
        this.cash = initialCash;
        this.shares = new HashMap<>();
        this.latestPrices = new HashMap<>();
        initializeStartingShares();
    }
    
    /**
     * Initialize portfolio with 10 shares of each stock
     */
    private void initializeStartingShares() {
        String[] stocks = {"AAPL", "NVDA", "TSLA", "AMZN"};
        for (String stock : stocks) {
            shares.put(stock, 10);
            latestPrices.put(stock, 100.0);
        }
    }
    
    /**
     * Buys one share of a stock at the given price
     */
    public boolean buy(String stockSymbol, double price) {
        if (cash >= price) {
            cash -= price;
            shares.put(stockSymbol, shares.getOrDefault(stockSymbol, 0) + 1);
            latestPrices.put(stockSymbol, price);
            return true;
        }
        return false;
    }
    
    /**
     * Sells one share of a stock at the given price
     */
    public boolean sell(String stockSymbol, double price) {
        int currentShares = shares.getOrDefault(stockSymbol, 0);
        if (currentShares > 0) {
            shares.put(stockSymbol, currentShares - 1);
            cash += price;
            latestPrices.put(stockSymbol, price);
            return true;
        }
        return false;
    }
    
    /**
     * Updates the latest price for a stock
     */
    public void updatePrice(String stockSymbol, double price) {
        latestPrices.put(stockSymbol, price);
    }
    
    /**
     * Calculates net worth (cash + value of all stock positions)
     */
    public double getNetWorth() {
        double totalValue = cash;
        for (Map.Entry<String, Integer> entry : shares.entrySet()) {
            String symbol = entry.getKey();
            int shareCount = entry.getValue();
            double price = latestPrices.getOrDefault(symbol, 0.0);
            totalValue += shareCount * price;
        }
        return totalValue;
    }
    
    /**
     * Gets net worth for a specific stock (shares at latest known price)
     */
    public double getNetWorth(String stockSymbol, double currentPrice) {
        int shareCount = shares.getOrDefault(stockSymbol, 0);
        return shareCount * currentPrice;
    }
    
    public double getCash() {
        return cash;
    }
    
    public int getShares(String stockSymbol) {
        return shares.getOrDefault(stockSymbol, 0);
    }
    
    public void setCash(double amount) {
        this.cash = amount;
    }
    
    public void setShares(String stockSymbol, int count) {
        if (count <= 0) {
            shares.remove(stockSymbol);
        } else {
            shares.put(stockSymbol, count);
        }
    }
}

