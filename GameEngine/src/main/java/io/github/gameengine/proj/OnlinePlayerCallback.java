package io.github.gameengine.proj;

import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.messagedata.MsgStatusCategory;
import io.github.API.utils.GsonWrapper;
import io.github.coreutils.proj.messages.Channels;
import io.github.coreutils.proj.messages.OnlineState;

import java.util.ArrayList;
import java.util.List;

public class OnlinePlayerCallback implements ISubscribeCallback {
    private final List<String> onlineList = new ArrayList<>();

    private List<OnlineState> constructList() {
        List<OnlineState> onlineStateList = new ArrayList<>();
        List<String> list = DBManager.getInstance().getAllUsers();
        list.forEach((userNames) -> {
            if (onlineList.contains(userNames)) {
                onlineStateList.add(new OnlineState(userNames, true));
            } else {
                onlineStateList.add(new OnlineState(userNames, false));
            }
        });
        return onlineStateList;
    }

    @Override
    public void status(MessagingAPI mApi, MsgStatus status) {
        if (status.getCategory().equals(MsgStatusCategory.MsgConnectedCategory)) {
            mApi.publish()
                    .message(constructList())
                    .channel(Channels.REQUEST_STATE.toString())
                    .execute();
        }
    }

    @Override
    public void resolved(MessagingAPI mApi, MsgResultAPI message) {
        if (message.getChannel().equals(Channels.ONLINE_STATE.toString())) {
            if (!message.getPublisherUuid().equals(mApi.getUuid())) {
                mApi.publish()
                        .message(constructList())
                        .channel(Channels.PRIVATE + message.getPublisherUuid() + Channels.BUILDER + Channels.REQUEST_STATE)
                        .execute();
            } else {
                OnlineState state = GsonWrapper.fromJson(message.getMessage(), OnlineState.class);
                if (state.isOnline()) {
                    if (!onlineList.contains(state.getUsername())) {
                        onlineList.add(state.getUsername());
                    }
                }
                mApi.publish()
                        .message(constructList())
                        .channel(Channels.REQUEST_STATE.toString())
                        .execute();
            }
        } else if (message.getChannel().equals(Channels.OFFLINE_STATE.toString())) {
            OnlineState state = GsonWrapper.fromJson(message.getMessage(), OnlineState.class);
            onlineList.removeIf((username) -> username.equals(state.getUsername()));
            mApi.publish()
                    .message(constructList())
                    .channel(Channels.REQUEST_STATE.toString())
                    .execute();
        }
    }

    @Override
    public void rejected(Exception e) {

    }
}
