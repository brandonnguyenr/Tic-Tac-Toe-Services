package io.github.coreutils.proj.messages;

import io.github.coreutils.proj.enginedata.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MoveRequestData {
    private final Board board;
    private final RoomData roomData;
    private final String currentPlayer;
}
