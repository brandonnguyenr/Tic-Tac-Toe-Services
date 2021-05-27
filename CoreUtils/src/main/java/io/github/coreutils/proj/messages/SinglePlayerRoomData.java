package io.github.coreutils.proj.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the end result of a single player game,
 * primarily used by Recorder service to write
 * a game to the database.
 */
@Getter
@Setter
@ToString
public class SinglePlayerRoomData {

    public enum RequestType {
        NORMAL,
        DISCONNECT
    }

    private int id;
    private PlayerData player = null;
    private PlayerData computer = new PlayerData();
    private long startTime;
    private long endTime;
    private RequestType request = RequestType.NORMAL;

    private boolean playerStart;
    private boolean playerWin;

    /**
     * Creates a SinglePlayerRoomData object.
     * @param id id of room, used to match moves made in this room
     * @param player the human player of this game
     * @param start time of start in miliseconds
     * @param end time of end in miliseconds
     * @param type RequestType.DISCONNECT is read by Recorder service as game end
     * @param playerStart true if player started
     * @param win true if player won
     * @param ai PlayerData.PlayerType for AI - either AI_EASY or AI_HARD
     */
    public SinglePlayerRoomData(int id, PlayerData player, long start, long end, RequestType type, boolean playerStart, boolean win, PlayerData.PlayerType ai) {
        this.id = id;
        this.player = player;
        this.startTime = start;
        this.endTime = end;
        this.request = type;
        this.playerStart = playerStart;
        this.playerWin = win;
        computer.setPlayerUserName("Computer");
        computer.setType((ai == PlayerData.PlayerType.HUMAN) ? PlayerData.PlayerType.AI_EASY : ai);
    }
}
