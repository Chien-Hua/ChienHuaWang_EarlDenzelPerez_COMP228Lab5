package exercise1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

public class MyGameFrame extends Application {
    public static final Insets STANDARD_INSETS = new Insets(3);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane myGamePane = new GridPane();
        myGamePane.setMinSize(640, 480);
        myGamePane.setStyle("-fx-background-color: white");
        myGamePane.setAlignment(Pos.TOP_LEFT);
        myGamePane.setPadding(STANDARD_INSETS);

        Button addGame = new Button("Add new game");
        Button editGame = new Button("Edit game");
        Button addPlayer = new Button("Add player");
        Button editPlayer = new Button("Edit player");

        Label information = new Label("Information");

        GridPane topMenu = new GridPane();
        GridPane botMenu = new GridPane();

        topMenu.setVgap(5);
        topMenu.setHgap(5);
        topMenu.setStyle("-fx-background-color: lightgray");
        topMenu.setPrefWidth(640);
        topMenu.setPadding(STANDARD_INSETS);

        topMenu.add(addGame, 0, 0);
        topMenu.add(editGame, 1, 0);
        topMenu.add(addPlayer, 2, 0);
        topMenu.add(editPlayer, 3, 0);

        botMenu.getChildren().addAll(information);
        botMenu.setStyle("-fx-background-color: lightgray");
        botMenu.setPadding(STANDARD_INSETS);
        botMenu.setPrefHeight(480);

        myGamePane.add(topMenu, 0, 0);
        myGamePane.add(botMenu, 0, 1);
        myGamePane.setMargin(topMenu, STANDARD_INSETS);
        myGamePane.setMargin(botMenu, STANDARD_INSETS);

        Scene mainScene = new Scene(myGamePane);


        primaryStage.setTitle("My Game - Score Organizer");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }
}
