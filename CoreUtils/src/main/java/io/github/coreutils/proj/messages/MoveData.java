package io.github.coreutils.proj.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MoveData {
    private int roomID;
    private String playerID; //UUID
    private int x;
    private int y;
    private long time;
}
