package com.stocktcg.roots;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

import com.stocktcg.stockview.StockView;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;

public class GameRoot {
    
    private Parent parentRoot;
    public GameRoot() {
        createGameScreen();
    }
    public Parent getParent() {
        return this.parentRoot;
    }
    public Button quitButton(){
        Button quitButton = new Button("Quit Game");
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
        this.parentRoot = gameBox;    
    }
    
    private HBox topBar(){
        HBox topBar = new HBox();
        Button quitButton = quitButton();

        Label netWorthLabel = new Label("Net Worth: $1000.00");
        Label casLabel = new Label("Cash: $500.00");
        netWorthLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        casLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        topBar.getChildren().addAll(quitButton, netWorthLabel, casLabel);
        topBar.setStyle("-fx-background-color: #1f193a; -fx-padding: 10;");
        return topBar;
    }
    
    private HBox stocksLayout(){
        VBox StockBox1 = new VBox();
        StockView apple = new StockView("AAPL");
        StockView nvidia = new StockView("NVDA");

        StockBox1.getChildren().add(apple);
        StockBox1.getChildren().add(nvidia);
        VBox StockBox2 = new VBox();
        StockView tesla = new StockView("TSLA");
        StockView amazon = new StockView("AMZN");
        StockBox2.getChildren().add(tesla);
        StockBox2.getChildren().add(amazon);
        HBox gameBox = new HBox();
        gameBox.getChildren().add(StockBox1);
        gameBox.getChildren().add(StockBox2);
        gameBox.setStyle("-fx-background-color: #1e1b29;");
        return gameBox;
   }
}
