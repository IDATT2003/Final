package com.stocktcg.roots;

import com.stocktcg.stockview.Graph;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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

        StackPane titleBox = new StackPane();
        Button startButton = startButton();
        startButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 30px; -fx-background-color: transparent; -fx-text-fill: white; -fx-border-radius: 5px; -fx-border-width: 2px; -fx-border-color: white;");
        
        Graph backgroundGraph = new Graph("", 100.0, 70, 2.3, 2.4);
        backgroundGraph.setOpacity(0.15);  // Make it very transparent so button is readable
        backgroundGraph.startAutoUpdate();  // Start the animation
        
        titleBox.getChildren().add(backgroundGraph);
        titleBox.getChildren().add(startButton);
        titleBox.setStyle("-fx-background-color: #1e1b29;");
        this.parentRoot = titleBox;
    }
    public void setupTitleScreen(Scene scene) {
        
        RootSwap.swapeRoot(scene, this.parentRoot);
    }
}
