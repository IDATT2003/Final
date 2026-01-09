package com.stocktcg;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.application.Application;
import javafx.stage.Stage;
public class Main extends Application {
    
    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Stock TCG");
        stage.show(); 
    }    
    public static void main(String[] args) {
        launch(args);
    }
    
}
