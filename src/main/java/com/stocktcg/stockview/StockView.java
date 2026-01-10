package com.stocktcg.stockview;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * StockView wires the Graph with game UI: live price ticker and buy/sell controls.
 */
public class StockView extends VBox {

    private final Graph graph;
    private final Label priceLabel = new Label();
    private final Label cashLabel = new Label();
    private final Label sharesLabel = new Label();
    private final Label netWorthLabel = new Label();
    private final Label statusLabel = new Label();
    private final Timeline ticker;

    private double cash = 100.0;
    private int shares = 0;

    public StockView() {
        super(10);
        this.graph = new Graph("Demo Stock", 100.0, 70);

        // Controls
        Button buyButton = new Button("Buy");
        Button sellButton = new Button("Sell");
        buyButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
        sellButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
        buyButton.setOnAction(e -> buy());
        sellButton.setOnAction(e -> sell());

        HBox controls = new HBox(10, buyButton, sellButton);
        controls.setAlignment(Pos.CENTER);

        // Style labels
        priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        cashLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        sharesLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        netWorthLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #ffcc00;");

        VBox stats = new VBox(10, priceLabel, cashLabel, sharesLabel, netWorthLabel, statusLabel);
        stats.setAlignment(Pos.TOP_LEFT);
        stats.setPadding(new Insets(10));

        VBox rightPanel = new VBox(20, controls, stats);
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setStyle("-fx-background-color: #1e1b29;");
        rightPanel.setPrefWidth(250);

        HBox layout = new HBox(graph, rightPanel);
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
        refreshStats();
    }

    private void buy() {
        double price = graph.getLatestClose();
        if (price <= 0) {
            statusLabel.setText("Price unavailable");
            return;
        }
        if (cash >= price) {
            cash -= price;
            shares += 1;
            statusLabel.setText("Bought 1 share at $" + String.format("%.2f", price));
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
        if (shares > 0) {
            shares -= 1;
            cash += price;
            statusLabel.setText("Sold 1 share at $" + String.format("%.2f", price));
        } else {
            statusLabel.setText("No shares to sell");
        }
        refreshStats();
    }

    private void refreshStats() {
        double price = graph.getLatestClose();
        double netWorth = cash + shares * price;
        priceLabel.setText("Price: $" + String.format("%.2f", price));
        cashLabel.setText("Cash: $" + String.format("%.2f", cash));
        sharesLabel.setText("Shares: " + shares);
        netWorthLabel.setText("Net Worth: $" + String.format("%.2f", netWorth));
    }

    /** Stops the ticker (call if leaving the view). */
    public void stop() {
        ticker.stop();
    }
}
