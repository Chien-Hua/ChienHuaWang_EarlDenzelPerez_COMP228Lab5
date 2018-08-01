package exercise1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MyGameFrame extends Application {
    public static final Insets STANDARD_INSETS = new Insets(3);
    public static final Insets HEADER_DISTANCE = new Insets(10,0,0,0);
    public static int MIN_WIDTH = 640;
    public static int MIN_HEIGHT = 480;

    public GridPane gameInformation = new GridPane();
    public GridPane playerInformation = new GridPane();
    public GridPane scoreInformation = new GridPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane myGamePane = new GridPane();
        myGamePane.setMinSize(MIN_WIDTH, MIN_HEIGHT);
        myGamePane.setStyle("-fx-background-color: white");
        myGamePane.setAlignment(Pos.TOP_LEFT);
        myGamePane.setPadding(STANDARD_INSETS);
        Button gameBtn = new Button("Games");
        Button playerBtn = new Button("Players");
        Button scoreBtn = new Button("Scores");
        GridPane topMenu = new GridPane();

        topMenu.setVgap(5);
        topMenu.setHgap(5);
        topMenu.setStyle("-fx-background-color: lightgray");
        topMenu.setPrefWidth(640);
        topMenu.setPadding(STANDARD_INSETS);
        topMenu.add(scoreBtn, 0, 0);
        topMenu.add(gameBtn, 1, 0);
        topMenu.add(playerBtn, 2, 0);

        //game information items
        Label gameHeader = new Label("Game Information");
        gameHeader.setFont(Font.font("Arial", 24));
        TextField gameName = new TextField();
        Button addNewGame = new Button("Add!");

        gameInformation.add(gameHeader, 0, 0, 3, 1);
        gameInformation.add(new Label("Add new game: "), 0, 1);
        gameInformation.add(gameName, 1, 1);
        gameInformation.add(addNewGame, 2, 1);
        gameInformation.setStyle("-fx-background-color: lightgray");
        gameInformation.setPadding(STANDARD_INSETS);
        gameInformation.setPrefHeight(MIN_HEIGHT);

        //player information items
        Label playerHeader = new Label("Player Information");
        playerHeader.setFont(Font.font("Arial", 24));
        TextField playerName = new TextField();
        Button addNewPlayer = new Button("Add!");

        playerInformation.add(playerHeader, 0, 0, 2, 1);
        playerInformation.add(new Label("Add new player: "), 0, 1);
        playerInformation.add(playerName, 1, 1);
        playerInformation.add(addNewPlayer, 2, 1);
        playerInformation.setStyle("-fx-background-color: lightgray");
        playerInformation.setPadding(STANDARD_INSETS);
        playerInformation.setPrefHeight(MIN_HEIGHT);

        //score information items
        Label addScoreHeader = new Label("Add new score");
        addScoreHeader.setFont(Font.font("Arial", 24));
        ComboBox playerForScore = new ComboBox();
        ComboBox gameForScore = new ComboBox();
        TextField textForScore = new TextField();
        Button addNewScore = new Button("Add score");
        Label latestScoreHeader = new Label("Latest scores");
        latestScoreHeader.setPadding(HEADER_DISTANCE);
        latestScoreHeader.setFont(Font.font("Arial", 24));

        scoreInformation.add(addScoreHeader, 0, 0, 2, 1);
        scoreInformation.add(new Label("Player: "), 0, 1);
        scoreInformation.add(playerForScore, 1, 1);
        scoreInformation.add(new Label("Game: "), 0, 2);
        scoreInformation.add(gameForScore, 1, 2);
        scoreInformation.add(new Label("Score: "), 0, 3);
        scoreInformation.add(textForScore, 1, 3);
        scoreInformation.add(addNewScore, 1,4);
        scoreInformation.add(latestScoreHeader, 0, 5, 2, 1);
        scoreInformation.setStyle("-fx-background-color: lightgray");
        scoreInformation.setPadding(STANDARD_INSETS);
        scoreInformation.setPrefHeight(MIN_HEIGHT);

        //add children to the main frame, myGamePane
        myGamePane.add(topMenu, 0, 0);
        myGamePane.add(scoreInformation, 0, 1);
        myGamePane.setMargin(topMenu, STANDARD_INSETS);
        myGamePane.setMargin(gameInformation, STANDARD_INSETS);
        myGamePane.setMargin(playerInformation, STANDARD_INSETS);

        //child = 1, denotes the body as it is added 2nd to our grid pane
        //both buttons remove the second child, and then add the appropriate pane
        gameBtn.setOnAction(e->{
            myGamePane.getChildren().remove(1);
            myGamePane.add(gameInformation, 0, 1);
        });

        playerBtn.setOnAction(e->{
            myGamePane.getChildren().remove(1);
            myGamePane.add(playerInformation, 0, 1);
        });

        scoreBtn.setOnAction(e->{
            myGamePane.getChildren().remove(1);
            myGamePane.add(scoreInformation, 0, 1);
        });

        Scene mainScene = new Scene(myGamePane);
        primaryStage.setTitle("My Game - Score Organizer");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }
}
