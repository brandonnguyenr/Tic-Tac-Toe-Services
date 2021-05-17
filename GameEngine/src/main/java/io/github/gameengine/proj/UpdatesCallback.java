package io.github.gameengine.proj;

import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.utils.GsonWrapper;
import io.github.coreutils.proj.messages.*;

public class UpdatesCallback implements ISubscribeCallback {
    @Override
    public void status(MessagingAPI messagingAPI, MsgStatus msgStatus) {

    }

    @Override
    public void resolved(MessagingAPI messagingAPI, MsgResultAPI msgResultAPI) {
        if (msgResultAPI.getChannel().equals(Channels.UPDATE_USERNAME.toString()) ||
                msgResultAPI.getChannel().equals(Channels.UPDATE_PERSONAL_INFO.toString()) ||
                msgResultAPI.getChannel().equals(Channels.UPDATE_PASSWORD.toString())) {

            UpdateData data = GsonWrapper.fromJson(msgResultAPI.getMessage(), UpdateData.class);

            try {
                if (msgResultAPI.getChannel().equals(Channels.UPDATE_USERNAME.toString())) {     //Checking if updating username
                    if (DBManager.getInstance().updateUsername(data)) {//checking if login is correct
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, true, "Username"))
                                .channel(Channels.PRIVATE + msgResultAPI.getPublisherUuid())
                                .execute();
                    } else {                                                //login unsuccessful
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, false, "Username"))
                                .channel(Channels.PRIVATE + msgResultAPI.getPublisherUuid())
                                .execute();
                    }
                } else if (msgResultAPI.getChannel().equals(Channels.UPDATE_PERSONAL_INFO.toString())) {//Checking if updating username
                    if (DBManager.getInstance().updatePersonalInfo(data)) {       //checking if login is correct
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, true, "PersonalInfo"))
                                .channel(Channels.PRIVATE + msgResultAPI.getPublisherUuid())
                                .execute();
                    } else {                                                //login unsuccessful
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, false, "PersonalInfo"))
                                .channel(Channels.PRIVATE + msgResultAPI.getPublisherUuid())
                                .execute();
                    }
                } else if (msgResultAPI.getChannel().equals(Channels.UPDATE_PASSWORD.toString())) {     //Checking if updating username
                    if (DBManager.getInstance().updatePassword(data)) {       //checking if login is correct
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, true, "Password"))
                                .channel(Channels.PRIVATE + msgResultAPI.getPublisherUuid())
                                .execute();
                    } else {                                                //login unsuccessful
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, false, "Password"))
                                .channel(Channels.PRIVATE + msgResultAPI.getPublisherUuid())
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
