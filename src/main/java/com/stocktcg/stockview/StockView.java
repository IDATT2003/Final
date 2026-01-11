package com.stocktcg.stockview;

import javafx.stage.Screen;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import com.stocktcg.Portfolio;

/**
 * StockView wires the Graph with game UI: live price ticker and buy/sell controls.
 * Uses global Portfolio for cash management.
 */
public class StockView extends VBox {

    private final Graph graph;
    private final Label priceLabel = new Label();
    private final Label statusLabel = new Label();
    private final Timeline ticker;
    private final Portfolio portfolio;
    private final Runnable onPortfolioChange;
    private final String stockSymbol;

    public StockView(String stockName, Portfolio portfolio, Runnable onPortfolioChange) {
        super(10);
        this.portfolio = portfolio;
        this.onPortfolioChange = onPortfolioChange;
        this.stockSymbol = stockName;
        this.graph = new Graph(stockName, 100.0, 70, 1, 1);

        // Controls
        Button buyButton = new Button("Buy");
        Button sellButton = new Button("Sell");
        this.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        buyButton.getStyleClass().add("buy-button");
        sellButton.getStyleClass().add("sell-button");
        buyButton.setOnAction(e -> buy());
        sellButton.setOnAction(e -> sell());

        VBox controls = new VBox(10, buyButton, sellButton);
        controls.setAlignment(Pos.CENTER);

        // Style labels
        priceLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: white;");
        statusLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #ffcc00;");

        VBox stats = new VBox(10, priceLabel, statusLabel);
        stats.setAlignment(Pos.TOP_LEFT);
        stats.setPadding(new Insets(10));

        VBox rightPanel = new VBox(20, controls, stats);
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setStyle("-fx-background-color: #1e1b29;");
        rightPanel.setPrefWidth(250);

        HBox layout = new HBox(graph, rightPanel);
        HBox.setMargin(graph, new Insets(0, 0, 0, 10));
        layout.setAlignment(Pos.CENTER_LEFT);

        setPadding(new Insets(0));
        setAlignment(Pos.CENTER);
        getChildren().add(layout);

        refreshStats();

        // Timeline to update price every second
        ticker = new Timeline(new KeyFrame(Duration.millis(500), e -> tickPrice()));
        ticker.setCycleCount(Timeline.INDEFINITE);
        ticker.play();
    }

    private void tickPrice() {
        graph.addRandomCandle();
        double currentPrice = graph.getLatestClose();
        portfolio.updatePrice(stockSymbol, currentPrice);
        refreshStats();
        onPortfolioChange.run();
    }

    private void buy() {
        double price = graph.getLatestClose();
        if (price <= 0) {
            statusLabel.setText("Price unavailable");
            return;
        }
        if (portfolio.buy(stockSymbol, price)) {
            statusLabel.setText("Bought 1 share at $" + String.format("%.2f", price));
            portfolio.updatePrice(stockSymbol, price);
            onPortfolioChange.run();
        } else {
            statusLabel.setText("Not enough cash to buy");
        }
        refreshStats();
    }

    private void sell() {
        double price = graph.getLatestClose();
        if (price <= 0) {
            statusLabel.setText("Price unavailable");
            return;
        }
        if (portfolio.sell(stockSymbol, price)) {
            statusLabel.setText("Sold 1 share at $" + String.format("%.2f", price));
            portfolio.updatePrice(stockSymbol, price);
            onPortfolioChange.run();
        } else {
            statusLabel.setText("No shares to sell");
        }
        refreshStats();
    }

    private void refreshStats() {
        double price = graph.getLatestClose();
        priceLabel.setText("Price: $" + String.format("%.2f", price));
    }

    /** Stops the ticker (call if leaving the view). */
    public void stop() {
        ticker.stop();
    }
}
