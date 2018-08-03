package exercise1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void addNewGame(int newID, String gameName) throws SQLException, IllegalStateException{
        statement = con.prepareStatement("INSERT into Game (game_id, game_title) VALUES (?,?)");
        statement.setInt(1,newID);
        statement.setString(2, gameName);
        statement.executeUpdate();
    }

    public void updateGame(String gameID, String newGameName) throws SQLException, IllegalStateException{
        statement = con.prepareStatement("UPDATE Game SET game_title = ? WHERE game_id = ? ");
        statement.setString(1, newGameName);
        statement.setInt(2, Integer.parseInt(gameID));
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
