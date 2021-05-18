package io.github.recorder.proj;

import com.google.gson.Gson;
import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.utils.GsonWrapper;
import io.github.coreutils.proj.messages.*;

public class RecorderCallback implements ISubscribeCallback {

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
        // MOVE MESSAGE (move made in a room)
        System.out.println("In RecorderCallback.resolved()");
        if (message.getChannel().equals(Channels.ROOM_MOVE.toString())) {
            System.out.println("(RecorderCallback) Received a MoveData message on Channels.ROOM_MOVE");
            MoveData move = GsonWrapper.fromJson(message.getMessage(), MoveData.class);

            // send message, output log if it fails
            if (!DBManager.getInstance().writeMove(move)) {
                System.out.println("(RecorderCallback) error writing move");
            }
        }
        // ROOM MESSAGE
        else if (message.getChannel().equals(Channels.ROOM.toString())) {
            System.out.println("(RecorderCallback) Received a RoomData message on Channels.ROOM");
            RoomData room = GsonWrapper.fromJson(message.getMessage(), RoomData.class);

            // send message, output log if it fails
            if (!DBManager.getInstance().writeRoom(room)) {
                System.out.println("(RecorderCallback) error writing room");
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