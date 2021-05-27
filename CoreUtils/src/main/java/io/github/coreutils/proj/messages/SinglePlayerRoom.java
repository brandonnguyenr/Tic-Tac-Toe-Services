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
@AllArgsConstructor
public class SinglePlayerRoom {
    private int id;
    private PlayerData player = null;
    private long startTime;
    private long endTime;

    private boolean playerStart;
    private boolean playerWin;
}
