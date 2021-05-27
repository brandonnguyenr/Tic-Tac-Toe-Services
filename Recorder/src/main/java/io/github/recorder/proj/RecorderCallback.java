package io.github.recorder.proj;

import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.utils.GsonWrapper;
import io.github.coreutils.proj.messages.*;

import java.util.HashMap;
import java.util.Map;

public class RecorderCallback implements ISubscribeCallback {

    //  ROOM ID    MOVE ARRAY
    Map<Integer, MoveData[]> multiplayerMoves;
    Map<Integer, MoveData[]> singleplayerMoves;

    public RecorderCallback() {
        multiplayerMoves  = new HashMap<>();
        singleplayerMoves = new HashMap<>();
    }

    @Override
    public void status(MessagingAPI mApi, MsgStatus status) {

    }

    /**
     * This function is the main part of the callback and will "catch" messages from the
     * stream, filtering them and preparing statements for the Recorder's DBManager.
     * <p>
     *     Handles messages that contain information that needs to be recorded in the Recorder
     *     database. This pertains mainly to every move made, every room that is created (start/end result),
     *     and creation of players (version of account that only has username).
     * </p>
     * @param mApi  the API that has the messages
     * @param message the message
     * @author Grant Goldsworth
     */
    @Override
    public void resolved(MessagingAPI mApi, MsgResultAPI message) {
        // filter the message. based on what type it is, create a JSON message to send to DB manager

        // MULTIPLAYER MOVE MESSAGE
        System.out.println("In RecorderCallback.resolved()");
        if (message.getChannel().equals(Channels.ROOM_MOVE.toString())) {
            System.out.println("(RecorderCallback) Received a MoveData message on Channels.ROOM_MOVE for multiplayer");
            MoveData move = GsonWrapper.fromJson(message.getMessage(), MoveData.class);

            // if the list of MP moves doesn't yet contain a list of moves for this room,
            // create one
            if (!multiplayerMoves.containsKey(move.getRoomID())) {
                multiplayerMoves.put(move.getRoomID(), new MoveData[9]);
                multiplayerMoves.get(move.getRoomID())[0] = move;
            }
            else {
                // go through array of moves for this room,
                // add it to the list
                MoveData[] l = multiplayerMoves.get(move.getRoomID());
                int i = 0;
                while (i <= l.length) {
                    if (l[i] == null) {
                        l[i] = move;
                        break;
                    }
                    i++;
                }
            }
        }

        // MOVES FOR SINGLE PLAYER
        else if (message.getChannel().equals(Channels.ROOM_MOVE_SINGLEPLAYER.toString())) {
            System.out.println("(RecorderCallback) got a message on Channels.ROOM_MOVE_SINGLEPLAYER");
            MoveData move = GsonWrapper.fromJson(message.getMessage(), MoveData.class);

            // if the list of MP moves doesn't yet contain a list of moves for this room,
            // create one
            if (!singleplayerMoves.containsKey(move.getRoomID())) {
                System.out.println("creating a new entry: " + move.getRoomID());
                singleplayerMoves.put(move.getRoomID(), new MoveData[9]);
//                singleplayerMoves.get(move.getRoomID())[0] = move;
            }
            // go through array of moves for this room,
            // add it to the list
            System.out.println("Adding move to room " + move.getRoomID());
            System.out.println(move + "\n");
            MoveData[] l = singleplayerMoves.get(move.getRoomID());
            int i = 0;
            while (i <= l.length) {
                if (l[i] == null) {
                    l[i] = move;
                    break;
                }
                i++;
            }

        }



        // ROOM MESSAGE
        else if (message.getChannel().equals(Channels.ROOM.toString())) {
            System.out.println("(RecorderCallback) Received a RoomData message on Channels.ROOM (ordinary)");
            RoomData room = GsonWrapper.fromJson(message.getMessage(), RoomData.class);

            // if room data is a disconnect, then write it and all of its moves
            if (room.getRequestType().equals(RoomData.RequestType.DISCONNECT)) {
                int databaseRoomID = DBManager.getInstance().writeRoom(room);

                // find the list of moves associated with this room and write them
                for (int i = 0; i < singleplayerMoves.get(room.getRoomID()).length; i ++) {
                    MoveData m = singleplayerMoves.get(room.getRoomID())[i];
                    if (m != null) {
                        m.setRoomID(databaseRoomID);
                        System.out.println(databaseRoomID);
                        System.out.println("Attempting to write move " + m);
                        if (!DBManager.getInstance().writeMove(m)) {
                            System.out.println("(RecorderCallback) [SPR] Error writing move: " + m);
                        }
                    }
                }

                // remove the room-moves pair from the list
                multiplayerMoves.remove(room.getRoomID(), multiplayerMoves.get(room.getRoomID()));
            }
        }

        // SINGLE PLAYER ROOM MESSAGE
        else if (message.getChannel().equals(Channels.ROOM_SINGLE_PLAYER.toString())) {
            System.out.println("(RecorderCallback) Received a RoomData message on Channels.ROOM_SINGLE_PLAYER");
            SinglePlayerRoomData room = GsonWrapper.fromJson(message.getMessage(), SinglePlayerRoomData.class);

            // if a disconnect
            if (room.getRequest().equals(SinglePlayerRoomData.RequestType.DISCONNECT)) {
                // send room message, output log if it fails
                RoomData model = new RoomData();
                PlayerData computer = new PlayerData("Computer", null);
                model.setPlayer1(room.getPlayer());
                model.setPlayer2(room.getComputer());
                model.setWinningPlayerID((room.isPlayerWin()) ? room.getPlayer() : room.getComputer());
                model.setStartingPlayerID((room.isPlayerStart()) ? room.getPlayer() : room.getComputer());
                model.setStartTime(room.getStartTime());
                model.setEndTime(room.getEndTime());
                System.out.println("Writing single player room " + room + "\n\n\n");
                int databaseRoomID = DBManager.getInstance().writeRoom(model);

                for (int i = 0; i < singleplayerMoves.get(room.getId()).length; i ++) {
                    System.out.println("DEBUG: RecorderCallback.java line 149 -- loop iteration " + i);
                    MoveData m = singleplayerMoves.get(room.getId())[i];
                    if (m != null) {
                        m.setRoomID(databaseRoomID);
//                        System.out.println(room.getId());
                        System.out.println("Attempting to write move " + m);
                        if (!DBManager.getInstance().writeMove(m)) {
                            System.out.println("(RecorderCallback) [SPR] Error writing move: " + m);
                        }
                    }
                }

                // remove the room-moves pair from the list
                singleplayerMoves.remove(room.getId());
                System.out.println("DEBUG: line 163 singlePlayerRooms still contains data for room " + singleplayerMoves.containsKey(room.getId()));

            }
        }

        // ACCOUNT CREATED MESSAGE
        else if (message.getChannel().equals(Channels.AUTHOR_CREATE.toString())) {
            System.out.println("Inside case for AUTHOR_CREATE loginData");
            LoginData logindata = GsonWrapper.fromJson(message.getMessage(), LoginData.class);

            if (!DBManager.getInstance().writeNewPlayer(logindata)) {
                System.out.println("(RecorderCallback) error writing player");
            }
        }

        // ACCOUNT UPDATED MESSAGE
        else if (message.getChannel().equals(Channels.UPDATE_USERNAME.toString())) {
            System.out.println("Inside case for UPDATE_USERNAME updateData");
            UpdateData updateData = GsonWrapper.fromJson(message.getMessage(), UpdateData.class);

            if (!DBManager.getInstance().updatePlayerUsername(updateData)) {
                System.out.println("(RecorderCallback) Something went wrong with update player");
            }
        }

        // ROOM DATA REQUEST MESSAGE
        else if (message.getChannel().equals(Channels.GET_ROOMS_DATA.toString())) {
            System.out.println("DEBUG: inside case for GET_REcORDER_DATA");
            PlayerData data = GsonWrapper.fromJson(message.getMessage(), PlayerData.class);

            // send the data to the appropriate method of DBManager
            // get the response from the method
            // send it back here
            //      response = getRoomsWithPlayer(data)
            //      send response back to client's callback
           mApi.publish()
                   .message(DBManager.getInstance().getRoomsWithPlayer(data))
                   .channel(Channels.PRIVATE + message.getPublisherUuid())
                   .execute();
        }

        // MOVES FOR A ROOM MESSAGE
        else if (message.getChannel().equalsIgnoreCase(Channels.GET_MOVES_DATA.toString())) {
            System.out.println("DEBUG Inside case for GET_MOVES_DATA");
            RoomData room = GsonWrapper.fromJson(message.getMessage(), RoomData.class);
            // get moves and send em back
            DBManager.getInstance().getMovesFromRoom(room);
            // publish message to API, send specifically to sender
            // TODO verify that the return of getMovesFromRoom (array list) is ok
            mApi.publish()
                    .message(DBManager.getInstance().getMovesFromRoom(room))
                    .channel(Channels.PRIVATE + message.getPublisherUuid())
                    .execute();
        }

    }

    @Override
    public void rejected(Exception e) {

    }
}