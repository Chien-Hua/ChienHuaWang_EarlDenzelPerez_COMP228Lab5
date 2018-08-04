package exercise1;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MyGameFrame extends Application {
    // set values for frames
    public static final Insets STANDARD_INSETS = new Insets(3);
    public static final Insets HEADER_DISTANCE = new Insets(10,0,0,0);
    public static final int NORMAL_GAP = 5;
    public static final int TEXTFIELD_WIDTH = 200;
    public static final int MIN_WIDTH = 640;
    public static final int MIN_HEIGHT = 480;
    public GridPane myGamePane = new GridPane(); // main pane
    public GridPane gameInformation = new GridPane();
    public GridPane editGameInformation;
    public GridPane addGameInformation = new GridPane();
    public GridPane playerInformation = new GridPane();
    public GridPane editPlayerInformation;
    public GridPane addPlayerInformation = new GridPane();
    public GridPane scoreInformation = new GridPane();
    public ScrollPane body;
    public MyGameDatabaseHandler dbHandler;
    private boolean gameDataPopulated = false;
    private boolean playerDataPopulated = false;

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
            Alert alert = new Alert(AlertType.ERROR, "Database driver not found");
            alert.show();
            System.exit(1);

        }
        catch(SQLException e){
            handleSQLException();
        }

        //main window settings
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
        topMenu.setPrefWidth(MIN_WIDTH);
        topMenu.setPadding(STANDARD_INSETS);
        topMenu.add(scoreBtn, 0, 0);
        topMenu.add(gameBtn, 1, 0);
        topMenu.add(playerBtn, 2, 0);

        //add game information
        Label addGamesHeader = new Label("Add Games");
        addGamesHeader.setFont(Font.font("Arial", 24));
        TextField gameName = new TextField();
        gameName.setPrefWidth(TEXTFIELD_WIDTH);
        Button addNewGame = new Button("Add!");
        addGameInformation.add(addGamesHeader, 0, 0, 3, 1);
        addGameInformation.add(new Label("Add new game: "), 0, 1);
        addGameInformation.add(gameName, 1, 1);
        addGameInformation.add(addNewGame, 2, 1);
        addGameInformation.setHgap(NORMAL_GAP);
        addGameInformation.setVgap(NORMAL_GAP);

        //game information items
        Label gameHeader = new Label("Games List");
        gameHeader.setFont(Font.font("Arial", 24));
        gameInformation.add(gameHeader, 0, 0);
        retrieveGamesMenu(addGameInformation);
        gameInformation.setStyle("-fx-background-color: lightgray");
        gameInformation.setPadding(STANDARD_INSETS);
        gameInformation.setPrefHeight(MIN_HEIGHT);
        gameInformation.setHgap(NORMAL_GAP);
        gameInformation.setVgap(NORMAL_GAP);
        
        //add player information
        Label addPlayersHeader = new Label("Add New Player");
        addPlayersHeader.setFont(Font.font("Arial", 24));
        TextField firstName = new TextField();
        firstName.setPrefWidth(TEXTFIELD_WIDTH);
        TextField lastName = new TextField();
        lastName.setPrefWidth(TEXTFIELD_WIDTH);
        TextField address = new TextField();
        address.setPrefWidth(2*TEXTFIELD_WIDTH);
        TextField provinceCode = new TextField();
        provinceCode.setPrefWidth(2);
        TextField postalCode = new TextField();
        postalCode.setPrefWidth(6);
        TextField phoneNumber = new TextField();
        phoneNumber.setPrefWidth(TEXTFIELD_WIDTH);
        Button addNewPlayer = new Button("Add!");
        addPlayerInformation.add(addPlayersHeader,0,0,2,1);
        addPlayerInformation.add(new Label("First name: "), 0, 1);
        addPlayerInformation.add(firstName, 1, 1);
        addPlayerInformation.add(new Label("Last name: "), 0, 2);
        addPlayerInformation.add(lastName, 1,2);
        addPlayerInformation.add(new Label("Address: "), 0, 3);
        addPlayerInformation.add(address, 1,3);
        addPlayerInformation.add(new Label("Province: "), 0,4);
        addPlayerInformation.add(provinceCode, 1, 4);
        addPlayerInformation.add(new Label("Postal Code: "), 0, 5);
        addPlayerInformation.add(postalCode, 1, 5);
        addPlayerInformation.add(new Label("Phone Number: "), 0, 6);
        addPlayerInformation.add(phoneNumber,1,6);        
        addPlayerInformation.add(addNewPlayer, 0, 7);
        addPlayerInformation.setHgap(NORMAL_GAP);
        addPlayerInformation.setVgap(NORMAL_GAP);

        //player information items
        Label playerHeader = new Label("Player List");
        playerHeader.setFont(Font.font("Arial", 24));
        playerInformation.add(playerHeader, 0, 0);
        retrievePlayersMenu(addPlayerInformation);
        playerInformation.setStyle("-fx-background-color: lightgray");
        playerInformation.setPadding(STANDARD_INSETS);
        playerInformation.setPrefHeight(MIN_HEIGHT);
        playerInformation.setHgap(NORMAL_GAP);
        playerInformation.setVgap(NORMAL_GAP);

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
        scoreInformation.setHgap(NORMAL_GAP);
        scoreInformation.setVgap(NORMAL_GAP);

        //add top menu to the main frame, myGamePane
        myGamePane.add(topMenu, 0, 0);
        myGamePane.setMargin(topMenu, STANDARD_INSETS);

        //change body frame to default item
        changeFrame(scoreInformation);

        //handlers for all buttons
        gameBtn.setOnAction(e->{
            changeFrame(gameInformation);
            retrieveGamesMenu(addGameInformation);
        });
        playerBtn.setOnAction(e->{
            changeFrame(playerInformation);
        });
        scoreBtn.setOnAction(e->{
            changeFrame(scoreInformation);
        });
        addNewGame.setOnAction(e->{
            //if empty, show alert. else, call method to add game to database
            String gameToAdd = gameName.getText().trim();
            if (gameToAdd.isEmpty()){
                showAlertMessage(Alert.AlertType.ERROR, "Please provide a game name!");
            }
            else {
                try {
                    dbHandler.addNewGame(dbHandler.retrieveNewGameID(), gameToAdd);
                    showAlertMessage(AlertType.CONFIRMATION, "Game added!");
                }
                catch (SQLException ex) {
                    handleSQLException();
                }
            }
            gameName.setText("");
            changeFrame(gameInformation);
            retrieveGamesMenu(addGameInformation);
            gameBtn.requestFocus();
        });
        addNewPlayer.setOnAction(e->{
            //TODO
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

    public void retrieveGamesMenu(GridPane subPane){
        if (gameDataPopulated){
            gameInformation.getChildren().remove(2);
            gameInformation.getChildren().remove(1);
        }
        gameInformation.add(subPane, 0, 2);
        try {
            //inner grid pane stats
            GridPane gameList = new GridPane();
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setHalignment(HPos.CENTER);
            gameList.getColumnConstraints().add(column1);
            gameList.setHgap(NORMAL_GAP);
            gameList.setVgap(NORMAL_GAP);

            //populate grid pane with data if possible
            Label gameIDLabel = new Label("Game ID");
            gameIDLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            gameList.add(gameIDLabel, 0, 0);
            Label gameTitleLabel = new Label("Game Title");
            gameTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            gameList.add(gameTitleLabel, 1, 0);
            ResultSet resultSet = dbHandler.retrieveGames();
            if (!resultSet.next()){
                gameList.add(new Label("Game list empty!"), 1, 1, 2, 1);
            }
            else{
                int i = 0;
                do{
                    i++;
                    String gameID = resultSet.getString("game_id");
                    gameList.add(new Label(gameID), 0, i);
                    gameList.add(new Label(resultSet.getString("game_title")), 1, i);
                    Button editButton = new Button("Edit");
                    editButton.setOnAction(e->{
                        showEditGameScreen(gameID);
                    });
                    gameList.add(editButton, 2, i);
                } while (resultSet.next());
            }

            gameList.setPadding(STANDARD_INSETS);
            gameInformation.add(gameList, 0,1,3,1);
            gameDataPopulated = true;
            resultSet.close();
        }
        catch(SQLException e){
            handleSQLException();
        }
    }

    //retrieves and formats list of games
    public void retrievePlayersMenu(GridPane subPane){
        if (playerDataPopulated){
            playerInformation.getChildren().remove(2);
            playerInformation.getChildren().remove(1);
        }
        playerInformation.add(subPane, 0, 2);
        try {
            //inner grid pane stats
            GridPane playerList = new GridPane();
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setHalignment(HPos.CENTER);
            playerList.getColumnConstraints().add(column1);
            playerList.setHgap(NORMAL_GAP);
            playerList.setVgap(NORMAL_GAP);

            //populate grid pane with data if possible
            Label playerIDLabel = new Label("Player ID");
            playerIDLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            playerList.add(playerIDLabel, 0, 0);
            Label playerTitleLabel = new Label("Player Title");
            playerTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            playerList.add(playerTitleLabel, 1, 0);
            ResultSet resultSet = dbHandler.retrievePlayers();
            if (!resultSet.next()){
                playerList.add(new Label("Player list empty!"), 1, 1, 2, 1);
            }
            else{
                int i = 0;
                do{
                    i++;
                    String playerID = resultSet.getString("player_id");
                    playerList.add(new Label(playerID), 0, i);
                    String playerName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                    playerList.add(new Label(playerName), 1, i);
                    Button showButton = new Button("Profile");
                    Button editButton = new Button("Edit");                    
                    showButton.setOnAction(e->{
                        //showPlayerScreen(playerID);
                    });
                    editButton.setOnAction(e->{
                        showEditPlayerScreen(playerID);
                    });
                    playerList.add(showButton, 2, i);
                    playerList.add(editButton, 3, i);
                } while (resultSet.next());
            }

            playerList.setPadding(STANDARD_INSETS);
            playerInformation.add(playerList, 0,1,3,1);
            playerDataPopulated = true;
            resultSet.close();
        }
        catch(SQLException e){
            handleSQLException();
        }
    }

    //generic alert message
    public void showAlertMessage(AlertType a, String message){
        Alert alert = new Alert(a, message);
        alert.show();
    }

    //force sql disconnection when sql exception happens
    public void handleSQLException(){
        showAlertMessage(AlertType.ERROR, "There seems to be a database error that needs to be addressed!");
        dbHandler.disconnectFromDatabase();
        System.exit(1);

    }

    public void showEditGameScreen(String gameID){
        editGameInformation = new GridPane();
        Label editGameHeader = new Label("Editing game with id " + gameID);
        editGameHeader.setFont(Font.font("Arial", 24));
        TextField gameName;
        try{
            gameName = new TextField(dbHandler.retrieveGameName(gameID));
            gameName.setPrefWidth(TEXTFIELD_WIDTH);
            Button editGame = new Button("Edit!");
            editGameInformation.add(editGameHeader, 0, 0, 3, 1);
            editGameInformation.add(new Label("Edit game name: "), 0, 1);
            editGameInformation.add(gameName, 1, 1);
            editGameInformation.add(editGame, 2, 1);
            editGameInformation.setStyle("-fx-background-color: lightgray");
            editGameInformation.setPadding(STANDARD_INSETS);
            editGameInformation.setPrefHeight(MIN_HEIGHT);
            editGameInformation.setHgap(NORMAL_GAP);
            editGameInformation.setVgap(NORMAL_GAP);
            editGame.requestFocus();
            editGame.setOnAction(e ->{
                String gameToEdit = gameName.getText().trim();
                if (gameToEdit.isEmpty()){
                    showAlertMessage(Alert.AlertType.ERROR, "Please provide a game name!");
                }
                else {
                    try {
                        dbHandler.updateGame(gameID, gameToEdit);
                        showAlertMessage(AlertType.CONFIRMATION, "Game edited!");
                    }
                    catch (SQLException ex) {
                        handleSQLException();
                    }
                }
                changeFrame(gameInformation);
                retrieveGamesMenu(addGameInformation);

            });
        }
        catch(SQLException e){
            handleSQLException();
        }
        changeFrame(gameInformation);
        retrieveGamesMenu(editGameInformation);
    }

    public void showEditPlayerScreen(String playerID){
        editPlayerInformation = new GridPane();
        try{
            Label editPlayerHeader = new Label("Editing player with id " + playerID);
            editPlayerHeader.setFont(Font.font("Arial", 24));
            TextField firstName = new TextField();
            firstName.setPrefWidth(TEXTFIELD_WIDTH);
            TextField lastName = new TextField();
            lastName.setPrefWidth(TEXTFIELD_WIDTH);
            TextField address = new TextField();
            address.setPrefWidth(2*TEXTFIELD_WIDTH);
            TextField provinceCode = new TextField();
            provinceCode.setPrefWidth(2);
            TextField postalCode = new TextField();
            postalCode.setPrefWidth(6);
            TextField phoneNumber = new TextField();
            phoneNumber.setPrefWidth(TEXTFIELD_WIDTH);
            Button editPlayer = new Button("Edit!");

            ResultSet rs = dbHandler.retrievePlayerInfo(playerID);
            if (rs.next()) {
                firstName.setText(rs.getString("first_name"));
                lastName.setText(rs.getString("last_name"));
                address.setText(rs.getString("address"));
                postalCode.setText(rs.getString("postal_code"));
                provinceCode.setText(rs.getString("province"));
                phoneNumber.setText(rs.getString("phone_number"));
            }
            rs.close();

            editPlayerInformation.add(editPlayerHeader,0,0,2,1);
            editPlayerInformation.add(new Label("First name: "), 0, 1);
            editPlayerInformation.add(firstName, 1, 1);
            editPlayerInformation.add(new Label("Last name: "), 0, 2);
            editPlayerInformation.add(lastName, 1,2);
            editPlayerInformation.add(new Label("Address: "), 0, 3);
            editPlayerInformation.add(address, 1,3);
            editPlayerInformation.add(new Label("Province: "), 0,4);
            editPlayerInformation.add(provinceCode, 1, 4);
            editPlayerInformation.add(new Label("Postal Code: "), 0, 5);
            editPlayerInformation.add(postalCode, 1, 5);
            editPlayerInformation.add(new Label("Phone Number: "), 0, 6);
            editPlayerInformation.add(phoneNumber,1,6);
            editPlayerInformation.add(editPlayer, 1, 7);
            editPlayerInformation.setStyle("-fx-background-color: lightgray");
            editPlayerInformation.setPadding(STANDARD_INSETS);
            editPlayerInformation.setPrefHeight(MIN_HEIGHT);
            editPlayerInformation.setHgap(NORMAL_GAP);
            editPlayerInformation.setVgap(NORMAL_GAP);
            editPlayer.requestFocus();
            editPlayer.setOnAction(e ->{
                //String playerToEdit = playerName.getText().trim();
                //if (playerToEdit.isEmpty()){
                //    showAlertMessage(Alert.AlertType.ERROR, "Please provide a player name!");
                //}
                //else {
                //    try {
                //        dbHandler.updatePlayer(playerID, playerToEdit);
                //        showAlertMessage(AlertType.CONFIRMATION, "Player edited!");
                //    }
                //    catch (SQLException ex) {
                //        handleSQLException();
                //    }
                //}
                changeFrame(playerInformation);
                retrievePlayersMenu(addPlayerInformation);

            });
        }
        catch(SQLException e){
            handleSQLException();
        }
        changeFrame(playerInformation);
        retrievePlayersMenu(editPlayerInformation);
    }

}
