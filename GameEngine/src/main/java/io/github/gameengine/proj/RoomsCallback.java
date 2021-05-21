package io.github.gameengine.proj;

import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.messagedata.MsgStatusCategory;
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
        if (status.getCategory().equals(MsgStatusCategory.MsgConnectedCategory)) {
            mApi.publish()
                    .message(roomDataList)
                    .channel(Channels.REQUEST + Channels.ROOM_LIST.toString())
                    .execute();
        }
    }


    @Override
    public void resolved(MessagingAPI mApi, MsgResultAPI message) {
        if (message.getChannel().equals(Channels.ROOM_LIST.toString())) {
            System.out.println("RoomsCallback " + mApi.getUuid());
            System.out.println("publisher " + message.getPublisherUuid());
            System.out.println(message.getMessage());
            if (message.getMessage().contains("ping")) {
                mApi.publish()
                        .message(roomDataList)
                        .channel(Channels.PRIVATE + message.getPublisherUuid())
                        .execute();
            } else {
                mApi.publish()
                        .message(roomDataList)
                        .channel(Channels.REQUEST + Channels.ROOM_LIST.toString())
                        .execute();
            }
        }
    }

    @Override
    public void rejected(Exception e) {

    }
}
