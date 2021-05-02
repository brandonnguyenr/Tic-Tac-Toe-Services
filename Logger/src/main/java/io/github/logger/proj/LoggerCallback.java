package io.github.logger.proj;

import io.github.API.proj.ISubscribeCallback;
import io.github.API.proj.MessageResultAPI;
import io.github.API.proj.MessagingAPI;
import io.github.API.proj.utils.GsonWrapper;
import io.github.library.proj.messages.Channels;
import io.github.library.proj.messages.MoveData;
import io.github.library.proj.messages.RoomData;

public class LoggerCallback implements ISubscribeCallback {
    @Override
    public void resolved(MessagingAPI messagingAPI, MessageResultAPI messageResultAPI) {
        if (messageResultAPI.getChannel().equals(Channels.ROOM_MOVE.toString())) {
            MoveData move = GsonWrapper.fromJson(messageResultAPI.getMessage(), MoveData.class);

            if (!DBManager.getInstance().writeMove(move)) {
                System.out.println("error writing move");
            }
        } else if (messageResultAPI.getChannel().equals(Channels.ROOM.toString())) {
            RoomData room = GsonWrapper.fromJson(messageResultAPI.getMessage(), RoomData.class);

            if (!DBManager.getInstance().writeRoom(room)) {
                System.out.println("error writing room");
            }
        }
    }

    @Override
    public void rejected(Exception e) throws Exception {
        ISubscribeCallback.super.rejected(e);
    }
}
