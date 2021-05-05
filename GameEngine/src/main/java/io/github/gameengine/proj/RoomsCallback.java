package io.github.gameengine.proj;

import io.github.API.ISubscribeCallback;
import io.github.API.MessageResultAPI;
import io.github.API.MessagingAPI;
import io.github.API.utils.GsonWrapper;
import io.github.library.proj.messages.Channels;
import io.github.library.proj.messages.RoomData;

import java.util.List;

public class RoomsCallback implements ISubscribeCallback {
    private final List<RoomData> roomDataList;

    public RoomsCallback(List<RoomData> roomDataList) {
        this.roomDataList = roomDataList;
    }

    @Override
    public void resolved(MessagingAPI messagingAPI, MessageResultAPI messageResultAPI) {
        if (messageResultAPI.getChannel().equals(Channels.ROOM_LIST.toString())) {
            messagingAPI.publish()
                    .message(GsonWrapper.toJson(roomDataList.toArray()))
                    .channel(Channels.ROOM.toString())
                    .execute();
        }
    }

    @Override
    public void rejected(Exception e) throws Exception {
        ISubscribeCallback.super.rejected(e);
    }
}
