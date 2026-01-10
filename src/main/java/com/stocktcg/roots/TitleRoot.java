package com.stocktcg.roots;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.Parent;

public class TitleRoot{
    
    private Parent parentRoot;
    public TitleRoot() {
       createTitleScreen(); 
    }
    public Parent getParent() {
        return this.parentRoot;
    }
    private Button startButton(){
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> {
            GameRoot gameRoot = new GameRoot();
            RootSwap.swapeRoot(startButton.getScene(), gameRoot.getParent());
        });
        return startButton;
    }
    public void createTitleScreen() {

        HBox titleBox = new HBox();
        Button startButton = startButton();
        titleBox.getChildren().addAll(startButton);
        this.parentRoot = titleBox;
    }
    public void setupTitleScreen(Scene scene) {
        
        RootSwap.swapeRoot(scene, this.parentRoot);
    }
}
