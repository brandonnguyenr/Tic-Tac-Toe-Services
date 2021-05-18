package io.github.recorder.proj;

import io.github.API.MessagingAPI;
import io.github.coreutils.proj.messages.Channels;
import io.github.coreutils.proj.messages.MoveData;
import io.github.coreutils.proj.messages.PlayerData;
import io.github.coreutils.proj.messages.RoomData;

public class Recorder {
    private MessagingAPI api;
    private RecorderCallback callback;

    public Recorder() {
        api = new MessagingAPI();
        callback = new RecorderCallback();

            // add recorder to channel
            api.subscribe()
                    .channels(Channels.ROOM_MOVE.toString(),
                              Channels.ROOM.toString(),
                              Channels.AUTHOR_CREATE.toString(),
                              Channels.UPDATE.toString(),
                              Channels.UPDATE_USERNAME.toString(),
                              Channels.GET_ROOMS_DATA.toString(),
                              Channels.GET_MOVES_DATA.toString()
                            )
                    .execute();
            api.addEventListener(callback,
                    Channels.ROOM_MOVE.toString(),
                    Channels.ROOM.toString(),
                    Channels.AUTHOR_CREATE.toString(),
                    Channels.UPDATE.toString(),
                    Channels.UPDATE_USERNAME.toString(),
                    Channels.UPDATE_PERSONAL_INFO.toString(),
                    Channels.GET_ROOMS_DATA.toString(),
                    Channels.GET_MOVES_DATA.toString()
            );

        // hard coded test cases
        // TODO remove when done
//        testRoomMessage();
//        testMoveMessage(3,"granttest1b", 0,0, 1234);
//        testMoveMessage(3,"abean", 0,1, 1235);
//        testMoveMessage(3,"granttest1b", 0,2, 1236);
//        testMoveMessage(3,"abean", 1,1, 1237);
//        testGetRoomsMessage();
//        testGetMovesFromRoom();

    }


    public static void main(String[] args) {
        System.out.println("Started Recorder");
        new Recorder();
    }

    /** Contains code I (grant) used for testing how service handles room messages **/
    private void testRoomMessage() {
        System.out.println("Sending new room data to api...");
        RoomData message = new RoomData();
        PlayerData dummy1 = new PlayerData("someBean", null);
        PlayerData dummy2 = new PlayerData("granttest1b", null);
        message.setPlayer1(dummy1);
        message.setPlayer2(dummy2);
        message.setStartingPlayerID(dummy2);
        message.setWinningPlayerID(dummy2);
        message.setStartTime(4589676);
        message.setEndTime(987984446);
        System.out.println("dummy1 name: " + message.getPlayer1().getPlayerUserName());
        System.out.println("dummy2 name: " + message.getPlayer2().getPlayerUserName());
        api.publish().message(message).channel(Channels.ROOM.toString()).execute();
    }

    /** Contains test code I (grant) wrote for testing how service handles move messages **/
    private void testMoveMessage(int room, String player, int x, int y, long time) {
        System.out.println("Sending new MoveData to api...");
        MoveData move = new MoveData(room, player, x, y, time);
        api.publish().message(move).channel(Channels.ROOM_MOVE.toString()).execute();
    }

    /** get rooms with playername "granttest1b" **/
    private void testGetRoomsMessage() {
        System.out.println("sending request to service for granttest1b...");
        PlayerData message = new PlayerData("granttest1b", null);
        api.publish().message(message).channel(Channels.GET_ROOMS_DATA.toString()).execute();
        System.out.println("send the request...");
    }

    /** test getting moves from a room **/
    private void testGetMovesFromRoom() {
        RoomData data = new RoomData();
        data.setRoomID(3);
        System.out.println("Testing get moves from room #" + data.getRoomID());
        for (MoveData m : DBManager.getInstance().getMovesFromRoom(data)) {
            System.out.println("Move: " + m + "\n");
        }
    }
}
