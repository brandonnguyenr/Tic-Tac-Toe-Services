package io.github.library.proj.messages;

public class PlayerData {
    public int playerID;
    public String playerName;
    public Token playerToken;

    public PlayerData(int playerID, String playerName, Token playerToken) {
        this.playerID = playerID;
        this.playerName = playerName;
        this.playerToken = playerToken;
    }
}
