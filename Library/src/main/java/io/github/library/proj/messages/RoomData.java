package io.github.library.proj.messages;

public class RoomData {
    public PlayerData player1;
    public PlayerData player2;
    public int roomID;
    public long startTime;
    public long endTime;
    public PlayerData startingPlayerID;
    public PlayerData winningPlayerID;

    public RoomData(PlayerData player1, PlayerData player2, int roomID, long startTime, long endTime, PlayerData startingPlayerID, PlayerData winningPlayerID) {
        this.player1 = player1;
        this.player2 = player2;
        this.roomID = roomID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startingPlayerID = startingPlayerID;
        this.winningPlayerID = winningPlayerID;
    }
}
