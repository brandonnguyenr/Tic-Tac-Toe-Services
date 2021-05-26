package io.github.gameengine.proj;

import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.utils.GsonWrapper;
import io.github.coreutils.proj.messages.Ping;
import io.github.gameengine.proj.enginedata.Lobby;
import io.github.coreutils.proj.messages.Channels;
import io.github.coreutils.proj.messages.MoveRequestData;
import io.github.coreutils.proj.messages.RoomData;

import java.util.List;
import java.util.Map;

public class GamesCallback implements ISubscribeCallback {
    private final List<RoomData> roomDataList;
    private final Map<Integer, Lobby> lobbyMap;

    public GamesCallback(List<RoomData> roomDataList, Map<Integer, Lobby> lobbyMap) {
        this.roomDataList = roomDataList;
        this.lobbyMap = lobbyMap;
    }

    /*===============================HELPER METHODS START============================================*/
    private void publishRoomList(MessagingAPI api) {
        api.publish()
                .message(new Ping())
                .channel(Channels.ROOM_LIST.toString())
                .execute();
    }

    private void createRoom(MessagingAPI api, RoomData data) {
        final int roomID = GameEngine.getGameID();
        data.setRoomID(roomID);

        data.setRoomChannel(Channels.ROOM.toString() + roomID);
        lobbyMap.put(roomID, new Lobby(data));
        roomDataList.add(data);

        if (data.isOpen()) {
            publishRoomList(api);
        } else {
            // TODO: idk about this
            startGame(api, data);
        }
    }

    private void joinRoom(MessagingAPI api, RoomData data) {
        if (lobbyMap.containsKey(data.getRoomID()) &&
                lobbyMap.get(data.getRoomID()).getRoomData().isOpen()) {
            startGame(api, data);
        } else {
            // TODO: register player as spectator
        }
    }

    private void startGame(MessagingAPI api, RoomData data) {
        int roomID = data.getRoomID();

        var room = lobbyMap.get(roomID).getRoomData();

        if (room.getPlayer1() == null) {
            room.setPlayer1(data.getPlayer1());
        } else {
            room.setPlayer2(data.getPlayer2());
        }

        api.publish()   // notify player 1
                .message(room)
                .channel(room.getPlayer1().getChannel())
                .execute();

        api.publish()   // notify player 2
                .message(room)
                .channel(room.getPlayer2().getChannel())
                .execute();

        try {
            Thread.sleep(3000); // TODO: This is soo hacky.. need to rethink
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        api.publish()
                .message(new MoveRequestData(lobbyMap.get(roomID).getBoard(), room, room.getPlayer1().getPlayerUserName()))
                .channel(room.getRoomChannel())
                .execute();

        lobbyMap.get(roomID).toggleCurrentPlayer();
    }
    /*===============================HELPER METHODS END============================================*/

    @Override
    public void status(MessagingAPI mAPI, MsgStatus status) {

    }

    @Override
    public void resolved(MessagingAPI mAPI, MsgResultAPI message) {
        if (message.getChannel().equals(Channels.ROOM_REQUEST.toString()) &&
                !message.getPublisherUuid().equals(mAPI.getUuid())) {
            RoomData roomData = GsonWrapper.fromJson(message.getMessage(), RoomData.class);
            if (roomData.getRequestType().equals(RoomData.RequestType.DISCONNECT)) {
                if (roomDataList.removeIf(room -> room.hasPlayer(roomData.getPlayer1()))) {
                    lobbyMap.values().removeIf(lobby -> lobby.getRoomData().hasPlayer(roomData.getPlayer1()));
                    publishRoomList(mAPI);
                }
            } else if (roomData.getRoomID() == -1) {
                createRoom(mAPI, roomData);
            } else {
                joinRoom(mAPI, roomData);
            }
        }
    }

    @Override
    public void rejected(Exception e) {

    }
}
