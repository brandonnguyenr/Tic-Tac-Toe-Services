package io.github.gameengine.proj;

import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.messagedata.MsgStatusCategory;
import io.github.coreutils.proj.messages.Channels;
import io.github.coreutils.proj.messages.PlayerData;
import io.github.coreutils.proj.messages.RoomData;

import java.util.List;

public class RoomsCallback implements ISubscribeCallback {
    private final List<RoomData> roomDataList;

    public RoomsCallback(List<RoomData> roomDataList) {
        this.roomDataList = roomDataList;
        RoomData test = new RoomData();
        test.setTitle("hackers");
        test.addPlayer(new PlayerData());
        RoomData test2 = new RoomData();
        test2.setTitle("coders");
        test2.addPlayer(new PlayerData());
        this.roomDataList.add(test);
        this.roomDataList.add(test2);
    }

    @Override
    public void status(MessagingAPI mApi, MsgStatus status) {
        if (status.getCategory().equals(MsgStatusCategory.MsgConnectedCategory)) {
            mApi.publish()
                    .message(roomDataList)
                    .channel(Channels.ROOM_LIST.toString())
                    .execute();
        }
    }


    @Override
    public void resolved(MessagingAPI mApi, MsgResultAPI message) {
        if (message.getChannel().equals(Channels.ROOM_LIST.toString())) {
            if (!message.getPublisherUuid().equals(mApi.getUuid())) {
                mApi.publish()
                        .message(roomDataList)
                        .channel(Channels.ROOM_LIST.toString())
                        .execute();
            }
        }
    }

    @Override
    public void rejected(Exception e) {

    }
}
