package io.github.coreutils.proj.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveData {
    private int roomID;
    private String playerID; //UUID
    private int x;
    private int y;
    private long time;

    public MoveData(int roomID, String playerID, int x, int y, long time) {
        this.roomID = roomID;
        this.playerID = playerID;
        this.x = x;
        this.y = y;
        this.time = time;
    }
}
