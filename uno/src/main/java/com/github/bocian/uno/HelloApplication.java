package com.github.bocian.uno;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application
{
    @Override
    public void start(Stage stage)
    {
        Label label = new Label("Witaj!");
        Button button = new Button("Kliknij mnie");

        button.setOnAction(e -> label.setText("Kliknięto przycisk!"));

        VBox layout = new VBox(10, label, button);
        Scene scene = new Scene(layout, 300, 200);

        stage.setTitle("Aplikacja bez FXML");
        stage.setScene(scene);
        stage.show();
    }

    
    public static void main(String[] args)
    {
        Deck deck = new Deck();
        deck.printDeck();
        //launch();
    }
}