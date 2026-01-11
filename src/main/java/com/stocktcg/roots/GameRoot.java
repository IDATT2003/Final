package com.stocktcg.roots;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

import com.stocktcg.Portfolio;
import com.stocktcg.stockview.StockView;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

public class GameRoot {
    
    private Parent parentRoot;
    private Portfolio portfolio;
    private Label netWorthLabel;
    private Label cashLabel;
    private double previousNetWorth;
    
    public GameRoot() {
        this.portfolio = new Portfolio(1000.0);
        this.previousNetWorth = portfolio.getNetWorth();
        createGameScreen();
    }
    
    public Parent getParent() {
        return this.parentRoot;
    }
    
    public Button quitButton(){
        Button quitButton = new Button("Quit Game");
        quitButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px 10px; -fx-background-color: transparent; -fx-text-fill: white; -fx-border-radius: 5px; -fx-border-width: 2px; -fx-border-color: white;");
        quitButton.setOnAction(e -> {
            TitleRoot titleRoot = new TitleRoot();
            RootSwap.swapeRoot(quitButton.getScene(), titleRoot.getParent());
        });
        return quitButton;
    }
    
    public void createGameScreen() {
        VBox gameBox = new VBox();
        HBox topBar = topBar();
        HBox stocksBox = stocksLayout();
        gameBox.getChildren().addAll(topBar, stocksBox);
        gameBox.setStyle("-fx-background-color: #1e1b29;");
        this.parentRoot = gameBox;    
    }
    
    private HBox topBar(){
        HBox topBar = new HBox(20);
        Button quitButton = quitButton();

        netWorthLabel = new Label();
        cashLabel = new Label();
        updateTopBar();
        
        netWorthLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        cashLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        topBar.getChildren().addAll(quitButton, cashLabel, netWorthLabel);
        topBar.setStyle("-fx-background-color: #1e1b29; -fx-padding: 10;");
        topBar.setAlignment(Pos.CENTER_LEFT);
        return topBar;
    }
    
    public void updateTopBar() {
        cashLabel.setText(String.format("Cash: $%.2f", portfolio.getCash()));
        double netWorth = portfolio.getNetWorth();
        
        // Color based on gain/loss
        if (netWorth > previousNetWorth) {
            netWorthLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #43973b;");  // Green
        } else if (netWorth < previousNetWorth) {
            netWorthLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #b02d2d;");  // Red
        } else {
            netWorthLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");    // White
        }
        previousNetWorth = netWorth;
        netWorthLabel.setText(String.format("Net Worth: $%.2f", netWorth));
    }
    
    private HBox stocksLayout(){
        VBox StockBox1 = new VBox();
        StockView apple = new StockView("AAPL", portfolio, this::updateTopBar);
        StockView nvidia = new StockView("NVDA", portfolio, this::updateTopBar);

        StockBox1.getChildren().add(apple);
        StockBox1.getChildren().add(nvidia);
        VBox StockBox2 = new VBox();
        StockView tesla = new StockView("TSLA", portfolio, this::updateTopBar);
        StockView amazon = new StockView("AMZN", portfolio, this::updateTopBar);
        StockBox2.getChildren().add(tesla);
        StockBox2.getChildren().add(amazon);
        HBox gameBox = new HBox();
        gameBox.getChildren().add(StockBox1);
        gameBox.getChildren().add(StockBox2);
        gameBox.setStyle("-fx-background-color: #1e1b29;");
        return gameBox;
   }
}
