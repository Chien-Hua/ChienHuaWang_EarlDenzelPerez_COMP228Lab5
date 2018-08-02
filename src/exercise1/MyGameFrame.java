package exercise1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyGameFrame extends Application {
    public static final Insets STANDARD_INSETS = new Insets(3);
    public static final Insets HEADER_DISTANCE = new Insets(10,0,0,0);
    public static final int MIN_WIDTH = 640;
    public static final int MIN_HEIGHT = 480;
    public GridPane myGamePane = new GridPane();
    public GridPane gameInformation = new GridPane();
    public GridPane playerInformation = new GridPane();
    public GridPane scoreInformation = new GridPane();
    public ScrollPane body;
    public MyGameDatabaseHandler dbHandler;
    private boolean gameDataPopulated = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //connect to database
        try {
            dbHandler = new MyGameDatabaseHandler();
        }
        catch(ClassNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Database driver not found");
            alert.show();
            System.exit(1);

        }
        catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "There seems to be a database error that needs to be addressed!");
            alert.show();
            //tableModel.disconnectFromDatabase();
            System.exit(1);
        }

        //overall window settings
        myGamePane.setMinSize(MIN_WIDTH, MIN_HEIGHT);
        myGamePane.setStyle("-fx-background-color: white");
        myGamePane.setAlignment(Pos.TOP_LEFT);
        myGamePane.setPadding(STANDARD_INSETS);

        //top menu bar
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
        Label gameHeader = new Label("Games List");
        gameHeader.setFont(Font.font("Arial", 24));
        Label addGamesHeader = new Label("Add Games");
        addGamesHeader.setFont(Font.font("Arial", 24));
        TextField gameName = new TextField();
        Button addNewGame = new Button("Add!");
        gameInformation.add(gameHeader, 0, 0, 3, 1);
        gameInformation.add(addGamesHeader, 0, 2, 3, 1);
        gameInformation.add(new Label("Add new game: "), 0, 3);
        gameInformation.add(gameName, 1, 3);
        gameInformation.add(addNewGame, 2, 3);
        retrieveGames();
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

        //add top menu to the main frame, myGamePane
        myGamePane.add(topMenu, 0, 0);
        myGamePane.setMargin(topMenu, STANDARD_INSETS);

        //change body frame to default item
        changeFrame(scoreInformation);

        //handlers for all buttons
        gameBtn.setOnAction(e->{
            changeFrame(gameInformation);
            retrieveGames();
        });
        playerBtn.setOnAction(e->{
            changeFrame(playerInformation);
        });
        scoreBtn.setOnAction(e->{
            changeFrame(scoreInformation);
        });

        //setting stage
        Scene mainScene = new Scene(myGamePane);
        primaryStage.setTitle("My Game - Score Organizer");
        primaryStage.setScene(mainScene);
        primaryStage.show();

        //listeners on window close
        primaryStage.setOnHiding(e->{
            dbHandler.disconnectFromDatabase();
        });
    }

    //changes frame to selected information screen
    //scrollbar is added to account for possibly long data
    public void changeFrame(GridPane infoScreen){
        if(myGamePane.getChildren().contains(body)) {
            myGamePane.getChildren().remove(1);
        }
        body = new ScrollPane(infoScreen);
        infoScreen.setPrefWidth(MIN_WIDTH);
        myGamePane.add(body, 0, 1);
        myGamePane.setMargin(body, STANDARD_INSETS);
    }

    public void retrieveGames(){
        if (gameDataPopulated){
            gameInformation.getChildren().remove(5);
        }
        try {
            //inner grid pane stats
            GridPane gameList = new GridPane();
            ColumnConstraints column1 = new ColumnConstraints(50);
            ColumnConstraints column2 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
            column2.setHgrow(Priority.ALWAYS);
            ColumnConstraints column3 = new ColumnConstraints(50);
            gameList.getColumnConstraints().addAll(column1, column2, column3);

            //populate grid pane with data if possible
            int i = 0;
            gameList.add(new Label("Game ID"), 0, i);
            gameList.add(new Label("Game Title"), 1, i);
            ResultSet resultSet = dbHandler.retrieveGames();
            if (!resultSet.next()){
                gameList.add(new Label("Game list empty!"), 1, 1, 2, 1);
            }
            else{
                do{
                    i++;
                    gameList.add(new Label(resultSet.getString("game_id")), 0, i);
                    gameList.add(new Label(resultSet.getString("game_title")), 1, i);
                    gameList.add(new Button("Edit"), 2, i);
                } while (resultSet.next());
            }
            
            gameList.setPadding(STANDARD_INSETS);
            gameInformation.add(gameList, 0,1,3,1);
            gameDataPopulated = true;
            resultSet.close();
        }
        catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "There seems to be a database error that needs to be addressed!");
            alert.show();
            //tableModel.disconnectFromDatabase();
            System.exit(1);
        }

    }


}
