package io.github.coreutils.proj.messages;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class to model a response to client's query for room data pertaining to a specific player.
 * <p>
 *     This class is primarily used to create messages for the client's
 *     game history module. In that module, a table is presented
 *     with each row's columns populated by this class's data. The
 *     win/loss/tie result is determined by perspectivePlayer. For example, if
 *     perspectivePlayer matches the winning player of the room, "win" is produced.
 * </p>
 * @author Grant Goldsworth
 */
@Getter
@Setter
@ToString
public class RoomResponse {
    // RoomResponse is used in history module - this is used to determine how to display W/L/T
    private String perspectivePlayer;
    private String otherPlayer;
    private String startingPlayer;
    private String roomID;
    private String startTime;
    private String endTime;

    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private String winningPlayer;

    /**
     * Constructor. Takes a perspective player and a RoomData object to
     * take data from.
     * <p>
     *     This class is primarily used to create messages for the client's
     *     game history module. In that module, a table is presented
     *     with each row's columns populated by this class's data. The
     *     win/loss/tie result is determined by perspectivePlayer. For example, if
     *     perspectivePlayer matches the winning player of the room, "win" is produced.
     * </p>
     * @param perspectivePlayer "viewer" of this room; used to determine Win/Loss/Tie
     * @param room the room to copy data from.
     * @see RoomData
     */
    public RoomResponse(String perspectivePlayer, RoomData room) {
        this.perspectivePlayer = perspectivePlayer;
        // get the other player
        if (perspectivePlayer.equals(room.getPlayer1().getPlayerUserName()))
            this.otherPlayer = room.getPlayer2().getPlayerUserName();
        else
            this.otherPlayer = room.getPlayer1().getPlayerUserName();

        if (room.getWinningPlayerID() != null)
            this.winningPlayer = room.getWinningPlayerID().getPlayerUserName();

        this.startingPlayer = room.getStartingPlayerID().getPlayerUserName();

        this.roomID = Integer.toString(room.getRoomID());
        this.startTime = getFormattedTime(room.getStartTime());
        this.endTime = getFormattedTime(room.getEndTime());
    }

    /**
     * Returns a 24 hour version of the start time of this game. The
     * time is returned as a String in the format HH:MM:SS
     * @return a String
     */
    public String getFormattedTime(long time) {
        return String.format("%02d:%02d:%02d",
                (time / (1000*60*60)) % 24,
                (time / (1000*60)) % 60,
                (time / 1000) % 60
        );
    }

    /**
     * Returns the result of the game from the perspective of perspectivePlayer.
     * If the room this is based off of in the database has no ID set for the
     * winning player, that symbolizes a tie.
     * <p>
     *     Returns one of three strings:
     *     <ul>
     *         <li>"WIN": the perspective player won this game</li>
     *         <li>"LOSS": the perspective player lost this game</li>
     *         <li>"TIE": the game was a tie</li>
     *     </ul>
     * </p>
     * @return a String
     * @author Grant Goldsworth
     */
    private String getWinLossTie() {
        if (winningPlayer != null) {
            if (perspectivePlayer.equalsIgnoreCase(winningPlayer))
                return "WIN";
            else
                return "LOSS";
        }
        else
            return "TIE";
    }
}
