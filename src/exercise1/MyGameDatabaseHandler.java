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
