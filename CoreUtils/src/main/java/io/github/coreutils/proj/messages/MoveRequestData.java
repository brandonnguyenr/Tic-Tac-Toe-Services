package io.github.coreutils.proj.messages;

import io.github.coreutils.proj.enginedata.Board;
import io.github.coreutils.proj.enginedata.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MoveRequestData {
    private final Board board;
    private final RoomData roomData;
    private final String currentPlayer;
    private final Token winningToken;

    public MoveRequestData(Board board, RoomData roomData, String currentPlayer) {
        this.board = board;
        this.roomData = roomData;
        this.currentPlayer = currentPlayer;
        this.winningToken = null;
    }
}
