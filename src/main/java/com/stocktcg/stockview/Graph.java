package com.stocktcg.stockview;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A JavaFX component that displays candlestick charts for stock prices.
 * Green candles represent price increases, red candles represent price decreases.
 */
public class Graph extends VBox {
    
    private Canvas canvas;
    private List<CandleStick> candles;
    private Random random;
    private String stockName;
    private double minPrice, maxPrice;
    private final int maxCandles;
    private int CANDLE_WIDTH;
    private static final int PADDING = 60;
    private static final double UPWARD_DRIFT = 0.001;  // Adjust this to change gain/loss odds.
    private Timeline autoUpdateTicker;
    
    /**
     * Candlestick data: Open, High, Low, Close
     */
    public static class CandleStick {
        public double open, high, low, close;
        
        public CandleStick(double open, double high, double low, double close) {
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
        }
        
        public boolean isGain() {
            return close >= open;
        }
    }
    
    /**
     * Creates a new Graph with custom settings
     * @param stockName the name of the stock
     * @param initialPrice the starting price
     * @param numCandles the number of candlesticks to generate
     */
    public Graph(String stockName, double initialPrice, int numCandles, double heightScale, double widthScale) {
        this.stockName = stockName;
        this.random = new Random();
        this.candles = new ArrayList<>();
        this.maxCandles = numCandles;
        this.CANDLE_WIDTH = 3*(int)widthScale;
        
        // Create canvas
        double width = Screen.getPrimary().getBounds().getWidth() * 0.44 * widthScale;
        double height = Screen.getPrimary().getBounds().getHeight() * 0.47* heightScale;
        canvas = new Canvas(width, height);
        this.getChildren().add(canvas);
        
        // Generate candlestick data
        generateCandles(initialPrice, numCandles);
        
        // Draw the chart
        drawChart();
        
        // Style the container
    }
    
    /**
     * Generates candlestick data
     */
    private void generateCandles(double initialPrice, int numCandles) {
        candles.clear();
        double price = initialPrice;
        minPrice = initialPrice;
        maxPrice = initialPrice;
        
        for (int i = 0; i < numCandles; i++) {
            // Generate random open price near previous close
            double open = price;
            
            // Generate random high and low
            double random1 = random.nextDouble() * 0.06 - 0.03;  // -3% to +3%
            double random2 = random.nextDouble() * 0.06 - 0.03;
            
            double high = open * (1 + Math.max(random1, random2));
            double low = open * (1 + Math.min(random1, random2));
            
            // Generate close price
            double closeChange = (random.nextDouble() - 0.5) * 0.08 + UPWARD_DRIFT;  // -4% to +4% + upward drift
            double close = open * (1 + closeChange);
            
            // Ensure prices stay positive
            close = Math.max(close, 1.0);
            high = Math.max(Math.max(high, close), open);
            low = Math.min(Math.min(low, close), open);
            
            candles.add(new CandleStick(open, high, low, close));
            
            // Update price for next candle
            price = close;
            minPrice = Math.min(minPrice, low);
            maxPrice = Math.max(maxPrice, high);
        }
        
        // Add padding to min/max
        double padding = (maxPrice - minPrice) * 0.1;
        minPrice -= padding;
        maxPrice += padding;
    }

    /**
     * Recalculates min/max prices and padding based on all candles
     */
    private void recalcMinMax() {
        if (candles.isEmpty()) {
            minPrice = 0;
            maxPrice = 1;
            return;
        }
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (CandleStick c : candles) {
            min = Math.min(min, c.low);
            max = Math.max(max, c.high);
        }
        double padding = (max - min) * 0.1;
        minPrice = min - padding;
        maxPrice = max + padding;
    }

    /**
     * Appends a new random candle continuing from the last close
     */
    public void addRandomCandle() {
        if (candles.isEmpty()) {
            return;
        }
        double open = getLatestClose();

        double random1 = random.nextDouble() * 0.06 - 0.03;  // -3% to +3%
        double random2 = random.nextDouble() * 0.06 - 0.03;

        double high = open * (1 + Math.max(random1, random2));
        double low = open * (1 + Math.min(random1, random2));

        double closeChange = (random.nextDouble() - 0.5) * 0.08 + UPWARD_DRIFT;  // -4% to +4% + upward drift
        double close = open * (1 + closeChange);

        close = Math.max(close, 1.0);
        high = Math.max(Math.max(high, close), open);
        low = Math.min(Math.min(low, close), open);

        candles.add(new CandleStick(open, high, low, close));
        enforceMaxCandles();
        recalcMinMax();
        drawChart();
    }

    /**
     * Keeps only the most recent maxCandles entries
     */
    private void enforceMaxCandles() {
        while (candles.size() > maxCandles) {
            candles.remove(0);
        }
    }
    
    /**
     * Draws the candlestick chart
     */
    private void drawChart() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // Clear background
        gc.setFill(Color.web("#1e1b29"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Draw axes
        drawAxes(gc);
        
        // Draw candlesticks
        drawCandlesticks(gc);
    }
    
    /**
     * Draws the axes and labels
     */
    private void drawAxes(GraphicsContext gc) {
        gc.setStroke(Color.web("#c0bcdb"));
        gc.setLineWidth(2);
        
        // Draw axes
        gc.strokeLine(PADDING, canvas.getHeight() - PADDING, 
                     canvas.getWidth() - PADDING, canvas.getHeight() - PADDING);  // X axis
        gc.strokeLine(PADDING, PADDING, PADDING, canvas.getHeight() - PADDING);  // Y axis
        
        // Draw axis labels
        gc.setFill(Color.web("#c0bcdb"));
        gc.setFont(Font.font(12));
        gc.fillText(stockName, PADDING + 10, 30);
        
        // Draw price labels on Y axis
        gc.setFont(Font.font(10));
        for (int i = 0; i <= 5; i++) {
            double price = minPrice + (maxPrice - minPrice) * (i / 5.0);
            double y = canvas.getHeight() - PADDING - (i / 5.0) * (canvas.getHeight() - 2 * PADDING);
            gc.fillText(String.format("$%.1f", price), 5, y + 4);
        }
    }
    
    /**
     * Draws the candlesticks
     */
    private void drawCandlesticks(GraphicsContext gc) {
        double chartWidth = canvas.getWidth() - 2 * PADDING;
        double chartHeight = canvas.getHeight() - 2 * PADDING;
        double spacing = chartWidth / (candles.size() + 1);
        
        for (int i = 0; i < candles.size(); i++) {
            CandleStick candle = candles.get(i);
            double x = PADDING + (i + 1) * spacing;
            
            // Convert prices to Y coordinates
            double highY = canvas.getHeight() - PADDING - 
                          ((candle.high - minPrice) / (maxPrice - minPrice)) * chartHeight;
            double lowY = canvas.getHeight() - PADDING - 
                         ((candle.low - minPrice) / (maxPrice - minPrice)) * chartHeight;
            double openY = canvas.getHeight() - PADDING - 
                          ((candle.open - minPrice) / (maxPrice - minPrice)) * chartHeight;
            double closeY = canvas.getHeight() - PADDING - 
                           ((candle.close - minPrice) / (maxPrice - minPrice)) * chartHeight;
            
            // Set color based on gain or loss
            Color bodyColor = candle.isGain() ? Color.web("#43973b") : Color.web("#b02d2d");
            Color wickColor = candle.isGain() ? Color.web("#43973b") : Color.web("#b02d2d");
            
            // Draw wick (high-low line)
            gc.setLineWidth(1.2);
            gc.setStroke(wickColor);
            gc.strokeLine(x, highY, x, lowY);
            
            // Draw body (open-close rectangle)
            double bodyTop = Math.min(openY, closeY);
            double bodyHeight = Math.abs(closeY - openY);
            gc.setLineWidth(0.8);
            gc.setFill(bodyColor);
            gc.setStroke(wickColor);
            gc.fillRect(x - CANDLE_WIDTH / 2, bodyTop, CANDLE_WIDTH, Math.max(bodyHeight, 2));
            gc.strokeRect(x - CANDLE_WIDTH / 2, bodyTop, CANDLE_WIDTH, Math.max(bodyHeight, 2));
        }
    }
    
    /**
     * Adds a new candlestick to the chart
     * @param open opening price
     * @param high highest price
     * @param low lowest price
     * @param close closing price
     */
    public void addCandle(double open, double high, double low, double close) {
        candles.add(new CandleStick(open, high, low, close));
        enforceMaxCandles();
        recalcMinMax();
        drawChart();
    }
    
    /**
     * Gets the list of candlesticks
     * @return list of candlesticks
     */
    public List<CandleStick> getCandles() {
        return new ArrayList<>(candles);
    }
    
    /**
     * Gets the latest close price
     * @return the most recent close price
     */
    public double getLatestClose() {
        return candles.isEmpty() ? 0.0 : candles.get(candles.size() - 1).close;
    }
    
    /**
     * Starts auto-updating the graph with new candlesticks every 500ms
     */
    public void startAutoUpdate() {
        if (autoUpdateTicker != null) {
            return;  // Already running
        }
        autoUpdateTicker = new Timeline(new KeyFrame(Duration.millis(500), e -> addRandomCandle()));
        autoUpdateTicker.setCycleCount(Timeline.INDEFINITE);
        autoUpdateTicker.play();
    }
    
    /**
     * Stops auto-updating
     */
    public void stopAutoUpdate() {
        if (autoUpdateTicker != null) {
            autoUpdateTicker.stop();
            autoUpdateTicker = null;
        }
    }
}