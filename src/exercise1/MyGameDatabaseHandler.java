package exercise1;

import java.sql.*;
import java.time.LocalDate;

public class MyGameDatabaseHandler {

    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DATABASE_URL = "jdbc:sqlserver://localhost:1433;database=MyGame;integratedSecurity=true";
    private Connection con;
    private PreparedStatement statement;
    private boolean connected = false;

    //constructor handles connection to database
    public MyGameDatabaseHandler() throws SQLException, ClassNotFoundException{
        Class.forName(DRIVER);
        con = DriverManager.getConnection(DATABASE_URL);
        connected = true;
    }

    public ResultSet retrieveGames() throws SQLException, IllegalStateException{
        statement = con.prepareStatement("SELECT * from Game");
        return statement.executeQuery();
    }

    public ResultSet retrievePlayers() throws SQLException, IllegalStateException{
        statement = con.prepareStatement("SELECT * from Player");
        return statement.executeQuery();
    }

    public ResultSet retrieveScores() throws SQLException, IllegalStateException{
        statement = con.prepareStatement(
                "SELECT (first_name  + ' ' + last_name) as full_name, game_title, playing_date, score from PlayerAndGame " +
                        "JOIN Player on PlayerAndGame.player_id = Player.player_id " +
                        "JOIN Game on PlayerAndGame.game_id = Game.game_id");
        return statement.executeQuery();
    }

    public ResultSet retrieveScores(int playerID) throws SQLException, IllegalStateException{
        statement = con.prepareStatement(
                "SELECT (first_name  + ' ' + last_name) as full_name, game_title, playing_date, score from PlayerAndGame " +
                        "JOIN Player on PlayerAndGame.player_id = Player.player_id " +
                        "JOIN Game on PlayerAndGame.game_id = Game.game_id " +
                        "WHERE Player.player_id = " + playerID);
        return statement.executeQuery();
    }

    public int retrieveNewGameID() throws SQLException, IllegalStateException{
        int maxID = 0;
        statement = con.prepareStatement("SELECT MAX(game_id) from Game");
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            maxID = rs.getInt(1) + 1;
        }
        rs.close();
        return maxID;
    }

    public int retrieveNewPlayerID() throws SQLException, IllegalStateException{
        int maxID = 0;
        statement = con.prepareStatement("SELECT MAX(player_id) from Player");
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            maxID = rs.getInt(1) + 1;
        }
        rs.close();
        return maxID;
    }

    public int retrieveNewPlayerGameID() throws SQLException, IllegalStateException{
        int maxID = 0;
        statement = con.prepareStatement("SELECT MAX(player_game_id) from PlayerAndGame");
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            maxID = rs.getInt(1) + 1;
        }
        rs.close();
        return maxID;
    }

    public String retrieveGameName(String id) throws SQLException, IllegalStateException{
        String gameName = "";
        statement = con.prepareStatement("SELECT game_title from Game WHERE game_id = " + id);
        ResultSet rs = statement.executeQuery();
        if (rs.next()){
            gameName = rs.getString("game_title");
        }
        rs.close();
        return gameName;
    }

    public ResultSet retrievePlayerInfo(String id) throws SQLException, IllegalStateException{
        statement = con.prepareStatement("SELECT * from  Player WHERE player_id = " + id);
        return statement.executeQuery();
    }

    public void addNewGame(int newID, String gameName) throws SQLException, IllegalStateException{
        statement = con.prepareStatement("INSERT into Game (game_id, game_title) VALUES (?,?)");
        statement.setInt(1,newID);
        statement.setString(2, gameName);
        statement.executeUpdate();
    }

    public void addNewPlayer(int newID, String firstName, String lastName, String address,
                             String province, String zipCode, String phoneNumber) throws SQLException, IllegalStateException{
        statement = con.prepareStatement(
                "INSERT into Player (player_id, first_name, last_name, address, province, postal_code, phone_number) " +
                        "VALUES (?,?,?,?,?,?,?)");
        statement.setInt(1, newID);
        statement.setString(2, firstName);
        statement.setString(3, lastName);
        statement.setString(4, address);
        statement.setString(5, province);
        statement.setString(6, zipCode);
        statement.setString(7, phoneNumber);
        statement.executeUpdate();
    }

    public void addNewScore(int pgID, int gameID, int playerID, LocalDate playDate, int score) throws SQLException, IllegalStateException{
        statement = con.prepareStatement(
                "INSERT into PlayerAndGame (player_game_id, game_id, player_id, playing_date, score) VALUES(?,?,?,?,?)");
        statement.setInt(1, pgID);
        statement.setInt(2, gameID);
        statement.setInt(3, playerID);
        statement.setDate(4, Date.valueOf(playDate));
        statement.setInt(5, score);
        statement.executeUpdate();
    }

    public void updateGame(String gameID, String newGameName) throws SQLException, IllegalStateException{
        statement = con.prepareStatement("UPDATE Game SET game_title = ? WHERE game_id = ? ");
        statement.setString(1, newGameName);
        statement.setInt(2, Integer.parseInt(gameID));
        statement.executeUpdate();
    }

    public void updatePlayer(String playerID, String firstName, String lastName, String address,
                             String province, String zipCode, String phoneNumber) throws SQLException, IllegalStateException{
        statement = con.prepareStatement(
                "UPDATE Player SET first_name = ?, last_name = ?, address = ?, province = ?, postal_code = ?, phone_number = ? " +
                        "WHERE player_id = ?");
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, address);
        statement.setString(4, province);
        statement.setString(5, zipCode);
        statement.setString(6, phoneNumber);
        statement.setInt(7, Integer.parseInt(playerID));
        statement.executeUpdate();

    }



    public void disconnectFromDatabase(){
        if (connected){
            try{
                statement.close();
                con.close();
            }
            catch(SQLException e){
                e.printStackTrace();

            }
        }
    }
}
