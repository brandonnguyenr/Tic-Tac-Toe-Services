package io.github.gameengine.proj;

import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.utils.GsonWrapper;
import io.github.coreutils.proj.enginedata.Board;
import io.github.gameengine.proj.enginedata.Lobby;
import io.github.coreutils.proj.enginedata.Token;
import io.github.coreutils.proj.messages.Channels;
import io.github.coreutils.proj.messages.MoveData;
import io.github.coreutils.proj.messages.MoveRequestData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MoveCallback implements ISubscribeCallback {
    private final Map<Integer, Lobby> lobbyList;

    public MoveCallback(Map<Integer, Lobby> lobbyList) {
        this.lobbyList = lobbyList;
    }

    private boolean isValidMove(MoveData data) {
        Lobby lobby;
        int roomID = data.getRoomID();
        if (lobbyList.containsKey(roomID)) {
            lobby = lobbyList.get(roomID);
            return lobby.isRunning() && lobby.getCurrentPlayer().equals(data.getPlayerID());
        }
        return false;
    }

    private boolean isWinner(Board board, Token token) {
        for (int index = 0; index < board.BOARD_ROWS; index++) {
            if ((board.getUnderlyingBoard()[index][0] == board.getUnderlyingBoard()[index][1] &&
                    board.getUnderlyingBoard()[index][1] == board.getUnderlyingBoard()[index][2] &&
                    board.getUnderlyingBoard()[index][0] == token) ||
                    (board.getUnderlyingBoard()[0][index] == board.getUnderlyingBoard()[1][index] &&
                            board.getUnderlyingBoard()[1][index] == board.getUnderlyingBoard()[2][index] &&
                            board.getUnderlyingBoard()[0][index] == token)) {
                return true;
            }
        }
        return (board.getUnderlyingBoard()[0][0] == board.getUnderlyingBoard()[1][1] &&
                board.getUnderlyingBoard()[1][1] == board.getUnderlyingBoard()[2][2] &&
                board.getUnderlyingBoard()[0][0] == token) ||
                (board.getUnderlyingBoard()[0][2] == board.getUnderlyingBoard()[1][1] &&
                        board.getUnderlyingBoard()[1][1] == board.getUnderlyingBoard()[2][0] &&
                        board.getUnderlyingBoard()[0][2] == token);
    }


    @Override
    public void status(MessagingAPI mApi, MsgStatus status) {

    }

    @Override
    public void resolved(MessagingAPI mApi, MsgResultAPI message) {
        if (message.getChannel().equals(Channels.ROOM_MOVE.toString())) {
            MoveData data = GsonWrapper.fromJson(message.getMessage(), MoveData.class);
            int roomID = data.getRoomID();
            if (isValidMove(data)) {
                Lobby lobby = lobbyList.get(roomID);
                Token token = (data.getPlayerID().equals(lobby.getRoomData().getPlayer1().getPlayerID())) ? Token.X : Token.O;
                lobby.getBoard().updateToken(data.getX(), data.getY(), token);

                if (isWinner(lobby.getBoard(), token) || lobby.getBoard().isBoardFull()) {
                    mApi.publish()
                            .message(new MoveRequestData(lobby.getBoard(), lobby.getRoomData(), null))
                            .channel(lobby.getRoomData().getRoomChannel().toString())
                            .execute();
                    lobby.endGame();
                } else {
                    lobby.toggleCurrentPlayer();

                    List<String> outGoingChannels = new LinkedList<>();
                    outGoingChannels.add(lobby.getRoomData().getRoomChannel().toString());
                    if (lobby.getRoomData().getPlayer2().isAI())
                        outGoingChannels.add(lobby.getRoomData().getPlayer2().getChannel());

                    for (var channelName : outGoingChannels) {
                        mApi.publish()
                                .message(new MoveRequestData(lobby.getBoard(), lobby.getRoomData(), lobby.getCurrentPlayer()))
                                .channel(channelName)
                                .execute();
                    }
                }
            }
        }
    }

    @Override
    public void rejected(Exception e) {

    }
}
