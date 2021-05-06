package io.github.recorder.proj;

import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.utils.GsonWrapper;
import io.github.library.proj.messages.Channels;
import io.github.library.proj.messages.MoveData;
import io.github.library.proj.messages.RoomData;

public class RecorderCallback implements ISubscribeCallback {

    @Override
    public void status(MessagingAPI mApi, MsgStatus status) {

    }

    @Override
    public void resolved(MessagingAPI mApi, MsgResultAPI message) {
        if (message.getChannel().equals(Channels.ROOM_MOVE.toString())) {
            MoveData move = GsonWrapper.fromJson(message.getMessage(), MoveData.class);

            if (!DBManager.getInstance().writeMove(move)) {
                System.out.println("error writing move");
            }
        } else if (message.getChannel().equals(Channels.ROOM.toString())) {
            RoomData room = GsonWrapper.fromJson(message.getMessage(), RoomData.class);

            if (!DBManager.getInstance().writeRoom(room)) {
                System.out.println("error writing room");
            }
        }
    }

    @Override
    public void rejected(Exception e) {

    }
}
