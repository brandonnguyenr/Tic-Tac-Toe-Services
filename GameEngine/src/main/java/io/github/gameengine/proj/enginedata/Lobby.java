package io.github.gameengine.proj.enginedata;

import io.github.coreutils.proj.enginedata.Board;
import io.github.coreutils.proj.messages.RoomData;

public class Lobby {
    private final RoomData roomData;
    private final Board board;
    private String currentPlayer = "";  // UUID
    private boolean running = true;

    public Lobby(RoomData roomData) {
        this.roomData = roomData;
        board = new Board();
    }

    /*===============GETTERS START================*/
    public RoomData getRoomData() {
        return roomData;
    }

    public Board getBoard() {
        return board;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isRunning() {
        return running;
    }

    /*===============GETTERS END================*/

    /*===============SETTERS START================*/
    public void toggleCurrentPlayer() {
        currentPlayer = (currentPlayer.equals("") || currentPlayer.equals(roomData.getPlayer2().getPlayerUserName())) ?
                roomData.getPlayer1().getPlayerUserName() :
                roomData.getPlayer2().getPlayerUserName();
    }
    /*===============SETTERS END================*/

    public void endGame() {
        running = false;
    }
}
