package io.github.coreutils.proj.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class MoveData {
    private int roomID;
    private String playerUserName;
    private int x;
    private int y;
    private long time;
}
