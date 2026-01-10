package com.stocktcg.roots;

import javafx.scene.control.Button;

import com.stocktcg.stockview.Graph;

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
        Graph stockGraph = new Graph("Demo Stock", 150.0, 30);
        gameBox.getChildren().add(stockGraph);
        gameBox.getChildren().add(quitButton());
        this.parentRoot = gameBox;    
    }
}
