package io.github.coreutils.proj.messages;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class RoomData {
    public enum RequestType {
        NORMAL,
        DISCONNECT,
        DENIED,
    }

    private String title;
    @Setter(AccessLevel.NONE)
    private int playerCount = 0;
    private PlayerData player1 = null;
    private PlayerData player2 = null;

    private int roomID = -1;
    private String roomChannel = null;
    private RequestType requestType = RequestType.NORMAL;

    private long startTime;
    private long endTime;

    private PlayerData startingPlayerID;
    private PlayerData winningPlayerID;

    public void setPlayer1(PlayerData player1) {
        if (this.player1 == null)
            incPlayerCount();
        this.player1 = player1;
    }

    public void setPlayer2(PlayerData player2) {
        if (this.player2 == null)
            incPlayerCount();
        this.player2 = player2;
    }

    public void incPlayerCount() {
        playerCount++;
    }

    public void addPlayer(PlayerData player) {
        if (getPlayer1() == null) {
            setPlayer1(player);
            incPlayerCount();
        } else if (getPlayer2() == null) {
            setPlayer2(player);
            incPlayerCount();
        } else {
            throw new IllegalArgumentException("Game Room is full!");
        }
    }



    public boolean hasPlayer(PlayerData player) {
        return player != null && (player.equals(getPlayer1()) || player.equals(getPlayer2()));
    }

    public boolean isOpen() {
        return player1 == null || player2 == null;
    }

    @Override
    public String toString() {
        return "RoomData{" +
                "player1=" + player1 +
                ", player2=" + player2 +
                ", roomID=" + roomID +
                ", roomChannel=" + roomChannel +
                ", requestType=" + requestType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", startingPlayerID=" + startingPlayerID +
                ", winningPlayerID=" + winningPlayerID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomData)) return false;
        RoomData roomData = (RoomData) o;
        return getRoomID() == roomData.getRoomID() &&
                getStartTime() == roomData.getStartTime() &&
                getEndTime() == roomData.getEndTime() &&
                Objects.equals(getPlayer1(), roomData.getPlayer1()) &&
                Objects.equals(getPlayer2(), roomData.getPlayer2()) &&
                getRoomChannel().equals(roomData.getRoomChannel()) &&
                getRequestType() == roomData.getRequestType() &&
                Objects.equals(getStartingPlayerID(), roomData.getStartingPlayerID()) &&
                Objects.equals(getWinningPlayerID(), roomData.getWinningPlayerID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getPlayer1(), getPlayer2(),
                getRoomID(), getRoomChannel(),
                getRequestType(), getStartTime(),
                getEndTime(), getStartingPlayerID(),
                getWinningPlayerID()
        );
    }
}
