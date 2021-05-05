package io.github.library.proj.messages;

import io.github.library.proj.enginedata.Board;
import lombok.Getter;

@Getter
public class MoveRequestData {
    private final Board board;
    private final RoomData roomData;
    private final String currentPlayer;

    public MoveRequestData(Board board, RoomData roomData, String currentPlayer) {
        this.board = board;
        this.roomData = roomData;
        this.currentPlayer = currentPlayer;
    }
}
