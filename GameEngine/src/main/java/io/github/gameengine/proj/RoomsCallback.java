package io.github.gameengine.proj;

import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.coreutils.proj.messages.Channels;
import io.github.coreutils.proj.messages.RoomData;

import java.util.List;

public class RoomsCallback implements ISubscribeCallback {
    private final List<RoomData> roomDataList;

    public RoomsCallback(List<RoomData> roomDataList) {
        this.roomDataList = roomDataList;
    }

    @Override
    public void status(MessagingAPI mApi, MsgStatus status) {

    }

    @Override
    public void resolved(MessagingAPI mApi, MsgResultAPI message) {
        if (message.getChannel().equals(Channels.ROOM_LIST.toString())) {
            mApi.publish()
                    .message(roomDataList.toArray())
                    .channel(Channels.ROOM.toString())
                    .execute();
        }
    }

    @Override
    public void rejected(Exception e) {

    }
}
