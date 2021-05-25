package io.github.coreutils.proj.messages;

import io.github.coreutils.proj.enginedata.Token;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class PlayerData {
    public enum PlayerType {
        HUMAN,
        AI_EASY,
        AI_HARD,
    }

    private String playerUserName = null;
    private Token playerToken = Token.BLANK;
    private String channel = null;
    @Setter(AccessLevel.NONE)
    private PlayerType type = PlayerType.HUMAN;


    public PlayerData() {
        // empty
    }

    public PlayerData(String playerID, String playerUserName, Token playerToken) {
        this.playerUserName = playerUserName;
        this.playerToken = playerToken;
    }

    public PlayerData(PlayerData original) {
        this.playerUserName = original.playerUserName;
        this.playerToken = original.playerToken;
        this.channel = original.channel;
        this.type = original.type;
    }

    public boolean isAI() {
        return type == PlayerType.AI_EASY || type == PlayerType.AI_HARD;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "playerName='" + playerUserName + '\'' +
                ", playerToken=" + playerToken +
                ", channel='" + channel + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerData)) return false;
        PlayerData that = (PlayerData) o;
        return Objects.equals(playerUserName, that.playerUserName) &&
                playerToken == that.playerToken &&
                Objects.equals(channel, that.channel) &&
                getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerUserName, playerToken, channel, getType());
    }
}
