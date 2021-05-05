package io.github.gameengine.proj;

import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.utils.GsonWrapper;
import io.github.library.proj.messages.Channels;
import io.github.library.proj.messages.LoginData;
import io.github.library.proj.messages.LoginResponseData;

public class AuthorizationCallback implements ISubscribeCallback {

    @Override
    public void status(MessagingAPI messagingAPI, MsgStatus msgStatus) {

    }

    @Override
    public void resolved(MessagingAPI messagingAPI, MsgResultAPI msgResultAPI) {
        if (msgResultAPI.getChannel().equals(Channels.AUTHOR_VALIDATE.toString()) ||
                msgResultAPI.getChannel().equals(Channels.AUTHOR_CREATE.toString())) {
            LoginData data = GsonWrapper.fromJson(msgResultAPI.getMessage(), LoginData.class);
            try {
                if (msgResultAPI.getChannel().equals(Channels.AUTHOR_VALIDATE.toString())) {
                    if (true) {     // TODO: database validation
                        messagingAPI.publish()
                                .message(GsonWrapper.toJson(new LoginResponseData(new LoginData(data.getUsername(), data.getFirstName(), data.getLastName()), true, null)))
                                .channel(Channels.PRIVATE.toString())       // TODO: figure our logic here
                                .execute();
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void rejected(Exception e) {

    }
}
