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
    public void status(MessagingAPI mApi, MsgStatus status) {

    }

    @Override
    public void resolved(MessagingAPI mApi, MsgResultAPI message) {
        if (message.getChannel().equals(Channels.AUTHOR_VALIDATE.toString()) ||
                message.getChannel().equals(Channels.AUTHOR_CREATE.toString())) {
            LoginData data = GsonWrapper.fromJson(message.getMessage(), LoginData.class);
            try {
                if (message.getChannel().equals(Channels.AUTHOR_VALIDATE.toString())) {
                    if (false) {     // TODO: username and password validation { DB call }
                        mApi.publish()
                                .message(new LoginResponseData(data, true, null))
                                .channel(Channels.PRIVATE + message.getPublisherUuid())
                                .execute();
                    } else {
                        mApi.publish()
                                .message(new LoginResponseData(data, false, "Invalid username/password"))
                                .channel(Channels.PRIVATE + message.getPublisherUuid())
                                .execute();
                    }
                } else if (message.getChannel().equals(Channels.AUTHOR_CREATE.toString())) {
                    if (false) {     // Todo: if doesnt already exists { DB call }
                        // TODO: added user data to database { DB call }
                        mApi.publish()
                                .message(new LoginResponseData(data, true, null))
                                .channel(Channels.PRIVATE + message.getPublisherUuid())
                                .execute();
                    } else {
                        mApi.publish()
                                .message(new LoginResponseData(data, false, "user already exists"))
                                .channel(Channels.PRIVATE + message.getPublisherUuid())
                                .execute();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                mApi.publish()
                            .message(new LoginResponseData(data, false, "there was an error"))
                            .channel(Channels.PRIVATE + message.getPublisherUuid())
                            .execute();
            }
        }
    }

    @Override
    public void rejected(Exception e) {

    }
}
