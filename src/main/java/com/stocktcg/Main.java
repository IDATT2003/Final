package com.stocktcg;


import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import com.stocktcg.roots.TitleRoot;

import javafx.application.Application;
import javafx.stage.Stage;
public class Main extends Application {
    
    @Override
    public void start(Stage stage) {
        TitleRoot titleRoot = new TitleRoot();
        Scene scene = new Scene(titleRoot.getParent(),800,600);
        stage.setScene(scene);
        stage.setTitle("Stock TCG");
        stage.show(); 
    }    
    public static void main(String[] args) {
        launch(args);
    }
    
}
