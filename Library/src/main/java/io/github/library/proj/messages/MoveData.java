package io.github.library.proj.messages;

public class MoveData {
    public int roomID;
    public int playerID;
    public int x;
    public int y;
    public long time;

    public MoveData(int roomID, int playerID, int x, int y, long time) {
        this.roomID = roomID;
        this.playerID = playerID;
        this.x = x;
        this.y = y;
        this.time = time;
    }
}
