package io.github.gameengine.proj;


import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.utils.GsonWrapper;
import io.github.coreutils.proj.messages.Channels;
import io.github.coreutils.proj.messages.LoginData;
import io.github.coreutils.proj.messages.LoginResponseData;

import java.sql.SQLException;

public class AuthorizationCallback implements ISubscribeCallback{

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

//                    String userName = "utsavp";
//                    String firstName = "utsav";
//                    String lastName = "parajuli";
//                    String password = "heyo";

                    if (DBManager.getInstance().createAccount(data)) {
                            mApi.publish()
                                    .message(new LoginResponseData(data, true, "null"))
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
            }
        }
    }

    @Override
    public void rejected(Exception e) {

    }
}
