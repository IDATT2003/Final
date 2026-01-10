package com.stocktcg.roots;

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
        HBox gameBox = new HBox();
        StockView stockView = new StockView();
        gameBox.getChildren().add(stockView);
        //gameBox.getChildren().add(quitButton());
        gameBox.setStyle("-fx-background-color: #1e1b29;");
        this.parentRoot = gameBox;    
    }
}
