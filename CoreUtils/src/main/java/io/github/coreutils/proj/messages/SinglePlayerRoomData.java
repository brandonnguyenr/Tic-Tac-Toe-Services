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

    public enum PlayerResult {
        WIN,
        LOSS,
        TIE
    }

    private int id;
    private PlayerData player = null;
    private PlayerData computer = new PlayerData("Computer", null);
    private long startTime;
    private long endTime;
    private RequestType request = RequestType.NORMAL;
    private PlayerResult result = PlayerResult.LOSS;

    private boolean playerStart;

    /**
     * some stuff
     * @param id
     * @param player
     * @param start
     * @param end
     * @param type
     * @param playerStart
     * @param result
     * @param ai
     */
    public SinglePlayerRoomData(int id, PlayerData player, long start, long end, RequestType type, boolean playerStart, PlayerResult result, PlayerData.PlayerType ai) {
        this.id = id;
        this.player = player;
        this.startTime = start;
        this.endTime = end;
        this.request = type;
        this.playerStart = playerStart;
        this.result = result;
        computer.setType((ai == PlayerData.PlayerType.HUMAN) ? PlayerData.PlayerType.AI_EASY : ai);
    }
}
