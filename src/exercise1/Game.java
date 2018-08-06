package exercise1;

public class Game {
    private String gameTitle;
    private int gameID;

    public int getGameID() {
        return gameID;
    }

    public Game(String gameTitle, int gameID) {
        this.gameTitle = gameTitle;
        this.gameID = gameID;
    }

    @Override
    public String toString() {
        return gameTitle;
    }
}
