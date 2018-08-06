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
import javafx.scene.control.DatePicker;
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
import java.time.LocalDate;


public class MyGameFrame extends Application {
    // set values for frames
    public static final Insets STANDARD_INSETS = new Insets(3);
    public static final Insets HEADER_DISTANCE = new Insets(10,0,0,0);
    public static final int NORMAL_GAP = 5;
    public static final int TEXTFIELD_WIDTH = 200;
    public static final int MIN_WIDTH = 640;
    public static final int MIN_HEIGHT = 480;
    public static final String CANADIAN_ZIPCODE_REGEX = "[A-Z][0-9][A-Z][0-9][A-Z][0-9]";
    public GridPane myGamePane = new GridPane(); // main pane
    public GridPane gameInformation = new GridPane();
    public GridPane editGameInformation;
    public GridPane addGameInformation = new GridPane();
    public GridPane playerInformation = new GridPane();
    public GridPane showPlayerInformation;
    public GridPane editPlayerInformation;
    public GridPane addPlayerInformation = new GridPane();
    public GridPane scoreInformation = new GridPane();
    public GridPane addScoreInformation = new GridPane();
    public ScrollPane body;
    public Button gameBtn;
    public Button scoreBtn;
    public Button playerBtn;
    public MyGameDatabaseHandler dbHandler;
    private boolean gameDataPopulated = false;
    private boolean playerDataPopulated = false;
    private boolean scoreDataPopulated = false;
    private boolean comboBoxesPopulated = false;

    //javafx launch
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

        /* SETTING UP WINDOWS */

        //main window settings
        myGamePane.setMinSize(MIN_WIDTH, MIN_HEIGHT);
        myGamePane.setStyle("-fx-background-color: white");
        myGamePane.setAlignment(Pos.TOP_LEFT);
        myGamePane.setPadding(STANDARD_INSETS);

        //top menu bar
        gameBtn = new Button("Games");
        playerBtn = new Button("Players");
        scoreBtn = new Button("Scores");
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
        addScoreHeader.setPadding(HEADER_DISTANCE);
        ComboBox<Player> playerForScore = new ComboBox();
        ComboBox<Game> gameForScore = new ComboBox();
        TextField textForScore = new TextField();
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        Button addNewScore = new Button("Add score");
        addScoreInformation.add(addScoreHeader, 0, 0, 2, 1);
        addScoreInformation.add(new Label("Player: "), 0, 1);
        addScoreInformation.add(new Label("Game: "), 0, 2);
        addScoreInformation.add(new Label("Score: "), 0, 3);
        addScoreInformation.add(textForScore, 1, 3);
        addScoreInformation.add(new Label("Date played: "), 0, 4);
        addScoreInformation.add(datePicker, 1, 4);
        addScoreInformation.add(addNewScore, 1,5);
        addScoreInformation.add(gameForScore, 1, 2);
        addScoreInformation.add(playerForScore, 1, 1);
        refreshComboBoxes(playerForScore, gameForScore);
        addScoreInformation.setHgap(NORMAL_GAP);
        addScoreInformation.setVgap(NORMAL_GAP);
        
        //score list items
        Label scoreHeader = new Label("Score Information");
        scoreHeader.setFont(Font.font("Arial", 24));
        scoreInformation.add(scoreHeader, 0, 0);
        retrieveScoreList();
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

        /* SETTING UP HANDLERS FOR ALL TOP LAYER BUTTONS */

        //game submenu
        gameBtn.setOnAction(e->{
            changeFrame(gameInformation);
        });

        //player submenu
        playerBtn.setOnAction(e->{
            changeFrame(playerInformation);
        });

        //score submenu
        scoreBtn.setOnAction(e->{
            changeFrame(scoreInformation);
            retrieveScoreList();
            refreshComboBoxes(playerForScore,gameForScore);
        });

        //add new game
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
                    gameName.setText("");
                    changeFrame(gameInformation);
                    retrieveGamesMenu(addGameInformation);
                }
                catch (SQLException ex) {
                    handleSQLException();
                }
            }
            gameBtn.requestFocus();
        });

        //add new player button
        addNewPlayer.setOnAction(e->{
            String fName = firstName.getText().trim();
            String lName = lastName.getText().trim();
            String addr = address.getText().trim();
            String prvCode = provinceCode.getText().replaceAll("\\s+","").toUpperCase().trim();
            String zipCode = postalCode.getText().toUpperCase().trim();
            String phoneNum = phoneNumber.getText().trim();

            if (fName.isEmpty() && lName.isEmpty()){
                showAlertMessage(Alert.AlertType.ERROR, "Please provide a name!");
            }
            else if (prvCode.length() != 2){
                showAlertMessage(Alert.AlertType.ERROR, "Please input the two-character code associated with the province. For example, ON for Ontario");
            }
            else if (!zipCode.matches(CANADIAN_ZIPCODE_REGEX)){
                showAlertMessage(Alert.AlertType.ERROR, "Zip code must be a valid Canadian format zipcode");
            }
            else {
                try {
                    dbHandler.addNewPlayer(dbHandler.retrieveNewPlayerID(), fName, lName, addr, prvCode, zipCode, phoneNum);
                    showAlertMessage(AlertType.CONFIRMATION, "Player added!");
                    firstName.setText("");
                    lastName.setText("");
                    address.setText("");
                    provinceCode.setText("");
                    postalCode.setText("");
                    phoneNumber.setText("");
                    changeFrame(playerInformation);
                    retrievePlayersMenu(addPlayerInformation);
                }
                catch (SQLException ex) {
                    handleSQLException();
                }
            }
            playerBtn.requestFocus();
        });

        //add new score button
        addNewScore.setOnAction(e->{
            try {
                int gameID = gameForScore.getValue().getGameID();
                int playerID = playerForScore.getValue().getPlayerID();
                int score = Integer.parseInt(textForScore.getText());
                LocalDate localDate = datePicker.getValue();
                if (localDate == null){
                    showAlertMessage(AlertType.INFORMATION, "Date not set. Picking today's date instead.");
                    localDate = LocalDate.now();
                }
                dbHandler.addNewScore(dbHandler.retrieveNewPlayerGameID(), gameID, playerID, localDate, score);
                showAlertMessage(AlertType.CONFIRMATION, "Score added!");
                textForScore.setText("");
                datePicker.setValue(LocalDate.now());
                changeFrame(scoreInformation);
                retrieveScoreList();
            }
            catch (NumberFormatException ex){
                showAlertMessage(AlertType.ERROR, "Score has to be numerical");
            }
            catch (NullPointerException ex){
                showAlertMessage(AlertType.ERROR, "Please provide a game and a player");
            }
            catch (SQLException ex) {
                handleSQLException();
            }
            scoreBtn.requestFocus();
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

    //retrieves score list on score submenu
    public void retrieveScoreList(){
        if (scoreDataPopulated){
            scoreInformation.getChildren().remove(2);
            scoreInformation.getChildren().remove(1);
        }
        try {
            //inner grid pane stats
            GridPane scoreList = new GridPane();
            scoreList.setHgap(NORMAL_GAP);
            scoreList.setVgap(NORMAL_GAP);

            //populate grid pane with data if possible
            Label playerLabel = new Label("Player");
            playerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            scoreList.add(playerLabel, 0, 0);
            Label gameLabel = new Label("Game");
            gameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            scoreList.add(gameLabel, 1, 0);
            Label dateLabel = new Label("Date played");
            dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            scoreList.add(dateLabel, 2, 0);
            Label scoreLabel = new Label("Score");
            scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            scoreList.add(scoreLabel, 3, 0);
            ResultSet resultSet = dbHandler.retrieveScores();
            if (!resultSet.next()){
                scoreList.add(new Label("Score list empty!"), 1, 1, 2, 1);
            }
            else{
                int i = 0;
                do{
                    i++;
                    scoreList.add(new Label(resultSet.getString("full_name")), 0, i);
                    scoreList.add(new Label(resultSet.getString("game_title")), 1, i);
                    scoreList.add(new Label(resultSet.getString("playing_date")), 2, i);
                    scoreList.add(new Label(resultSet.getString("score")), 3, i);
                } while (resultSet.next());
            }

            scoreList.setPadding(STANDARD_INSETS);
            scoreInformation.add(scoreList, 0,1);
            scoreInformation.add(addScoreInformation, 0, 2);
            scoreDataPopulated = true;
            resultSet.close();
        }
        catch(SQLException e){
            handleSQLException();
        }
    }

    //retrieves game submenu
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
                    showButton.setOnAction(e->{
                        showPlayerScreen(playerID);
                        playerBtn.requestFocus();
                    });
                    playerList.add(showButton, 2, i);
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

    //shows edit game screen on game submenu
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
                gameBtn.requestFocus();


            });
        }
        catch(SQLException e){
            handleSQLException();
        }
        changeFrame(gameInformation);
        retrieveGamesMenu(editGameInformation);
    }

    //shows player screen and corresponding edit button
    public void showPlayerScreen(String playerID){
        showPlayerInformation = new GridPane();
        Label showPlayerHeader = new Label("Player information");
        showPlayerHeader.setFont(Font.font("Arial", 24));
        try{
            String firstName = "";
            String lastName = "";
            String address = "";
            String postalCode = "";
            String provinceCode = "";
            String phoneNumber = "";
            Button editPlayer = new Button("Edit!");

            ResultSet rs = dbHandler.retrievePlayerInfo(playerID);
            if (rs.next()) {
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
                address = rs.getString("address");
                postalCode = rs.getString("postal_code");
                provinceCode = rs.getString("province");
                phoneNumber = rs.getString("phone_number");
            }
            rs.close();

            showPlayerInformation.add(showPlayerHeader,0,0,2,1);
            showPlayerInformation.add(new Label("First name: "), 0, 1);
            showPlayerInformation.add(new Label(firstName), 1, 1);
            showPlayerInformation.add(new Label("Last name: "), 0, 2);
            showPlayerInformation.add(new Label(lastName), 1,2);
            showPlayerInformation.add(new Label("Address: "), 0, 3);
            showPlayerInformation.add(new Label(address), 1,3);
            showPlayerInformation.add(new Label("Province: "), 0,4);
            showPlayerInformation.add(new Label(provinceCode), 1, 4);
            showPlayerInformation.add(new Label("Postal Code: "), 0, 5);
            showPlayerInformation.add(new Label(postalCode), 1, 5);
            showPlayerInformation.add(new Label("Phone Number: "), 0, 6);
            showPlayerInformation.add(new Label(phoneNumber),1,6);
            showPlayerInformation.add(editPlayer, 1, 7);
            showPlayerInformation.setStyle("-fx-background-color: lightgray");
            showPlayerInformation.setPadding(STANDARD_INSETS);
            showPlayerInformation.setPrefHeight(MIN_HEIGHT);
            showPlayerInformation.setHgap(NORMAL_GAP);
            showPlayerInformation.setVgap(NORMAL_GAP);
            editPlayer.requestFocus();
            editPlayer.setOnAction(e ->{
                changeFrame(playerInformation);
                createEditPlayerScreen(playerID);
                retrievePlayersMenu(editPlayerInformation);
                playerBtn.requestFocus();

            });
        }
        catch(SQLException e){
            handleSQLException();
        }
        changeFrame(playerInformation);
        retrievePlayersMenu(showPlayerInformation);
    }

    //changes viewed details to edit mode and provides a button to edit player
    public void createEditPlayerScreen(String playerID){
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
                String fName = firstName.getText().trim();
                String lName = lastName.getText().trim();
                String addr = address.getText().trim();
                String prvCode = provinceCode.getText().replaceAll("\\s+","").toUpperCase().trim();
                String zipCode = postalCode.getText().toUpperCase().trim();
                String phoneNum = phoneNumber.getText().trim();

                if (fName.isEmpty() && lName.isEmpty()){
                    showAlertMessage(Alert.AlertType.ERROR, "Please provide a name!");
                }
                else if (prvCode.length() != 2){
                    showAlertMessage(Alert.AlertType.ERROR, "Please input the two-character code associated with the province. For example, ON for Ontario");
                }
                else if (!zipCode.matches(CANADIAN_ZIPCODE_REGEX)){
                    showAlertMessage(Alert.AlertType.ERROR, "Zip code must be a valid Canadian format zipcode");
                }
                else {
                    try {
                        dbHandler.updatePlayer(playerID, fName, lName, addr, prvCode, zipCode, phoneNum);
                        showAlertMessage(AlertType.CONFIRMATION, "Player edited!");
                        changeFrame(playerInformation);
                        retrievePlayersMenu(addPlayerInformation);
                    }
                    catch (SQLException ex) {
                        handleSQLException();
                    }
                }
                playerBtn.requestFocus();
            });
        }
        catch(SQLException e){
            handleSQLException();
        }
    }

    //refreshes comboboxes on score submenu
    public void refreshComboBoxes(ComboBox<Player> playerBox, ComboBox<Game> gameBox){
        if(comboBoxesPopulated) {
            addScoreInformation.getChildren().remove(9);
            addScoreInformation.getChildren().remove(8);
        }

        try{
            playerBox.getItems().clear();
            gameBox.getItems().clear();
            ResultSet gameSet = dbHandler.retrieveGames();
            while (gameSet.next()){
                Game game = new Game(gameSet.getString("game_title"), gameSet.getInt("game_id"));
                gameBox.getItems().add(game);
            }
            gameSet.close();
            ResultSet playerSet = dbHandler.retrievePlayers();
            while (playerSet.next()){
                String fullName = playerSet.getString("first_name") + " " + playerSet.getString("last_name");
                Player player = new Player(fullName, playerSet.getInt("player_id"));
                playerBox.getItems().add(player);
            }
            playerSet.close();
        }
        catch (SQLException ex) {
            handleSQLException();
        }
    }
}
