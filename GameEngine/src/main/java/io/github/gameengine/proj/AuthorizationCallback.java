package io.github.gameengine.proj;

import io.github.API.ISubscribeCallback;
import io.github.API.MessageResultAPI;
import io.github.API.MessagingAPI;
import io.github.API.utils.GsonWrapper;
import io.github.library.proj.messages.Channels;
import io.github.library.proj.messages.LoginData;
import io.github.library.proj.messages.LoginResponseData;

public class AuthorizationCallback implements ISubscribeCallback {

    @Override
    public void resolved(MessagingAPI messagingAPI, MessageResultAPI messageResultAPI) {
        if (messageResultAPI.getChannel().equals(Channels.AUTHOR_VALIDATE.toString()) ||
                messageResultAPI.getChannel().equals(Channels.AUTHOR_CREATE.toString())) {
            LoginData data = GsonWrapper.fromJson(messageResultAPI.getMessage(), LoginData.class);
            try {
                if (messageResultAPI.getChannel().equals(Channels.AUTHOR_VALIDATE.toString())) {
                    if (true) {     // TODO: database validation
                        messagingAPI.publish()
                                    .message(GsonWrapper.toJson(new LoginResponseData(new LoginData(data.getUsername(), data.getFirstName(), data.getLastName()), true, null)))
                                    .channel(Channels.PRIVATE.toString())       // TODO: figure our logic here
                                    .execute();
                    }
                }
            }
        }
    }

    @Override
    public void rejected(Exception e) throws Exception {
        ISubscribeCallback.super.rejected(e);
    }
}
