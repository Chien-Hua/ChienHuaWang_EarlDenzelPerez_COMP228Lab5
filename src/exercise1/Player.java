package exercise1;

public class Player {
    private String playerName;
    private int playerID;

    public int getPlayerID() {
        return playerID;
    }

    public Player(String playerName, int playerID) {
        this.playerName = playerName;
        this.playerID = playerID;
    }

    @Override
    public String toString() {
        return playerName;
    }
}
