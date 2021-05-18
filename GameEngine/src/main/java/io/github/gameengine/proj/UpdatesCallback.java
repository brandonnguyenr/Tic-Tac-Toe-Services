package io.github.gameengine.proj;

import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.utils.GsonWrapper;
import io.github.coreutils.proj.messages.*;

/**
 * Callback class for the calls that will update the player's account settings.
 * @author Utsav Parajuli
 */
public class UpdatesCallback implements ISubscribeCallback {
    @Override
    public void status(MessagingAPI messagingAPI, MsgStatus msgStatus) {
    }

    /**
     * This method will get the data from player and make calls to the database and publish the result
     * back to the particular api call.
     * @param messagingAPI: api
     * @param msgResultAPI: api containing the result
     */
    @Override
    public void resolved(MessagingAPI messagingAPI, MsgResultAPI msgResultAPI) {
        if (msgResultAPI.getChannel().equals(Channels.UPDATE_USERNAME.toString()) ||
                msgResultAPI.getChannel().equals(Channels.UPDATE_PERSONAL_INFO.toString()) ||
                msgResultAPI.getChannel().equals(Channels.UPDATE_PASSWORD.toString())) {

            UpdateData data = GsonWrapper.fromJson(msgResultAPI.getMessage(), UpdateData.class);

            try {
                if (msgResultAPI.getChannel().equals(Channels.UPDATE_USERNAME.toString())) {     //Checking if updating username
                    if (DBManager.getInstance().updateUsername(data)) {                          //checking if username updated
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, true, "Username"))
                                .channel(Channels.PRIVATE + msgResultAPI.getPublisherUuid())
                                .execute();
                    } else {                                                //update unsuccessful
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, false, "Username"))
                                .channel(Channels.PRIVATE + msgResultAPI.getPublisherUuid())
                                .execute();
                    }
                } else if (msgResultAPI.getChannel().equals(Channels.UPDATE_PERSONAL_INFO.toString())) {//Checking if updating info
                    if (DBManager.getInstance().updatePersonalInfo(data)) {                             //checking if info updated
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, true, "PersonalInfo"))
                                .channel(Channels.PRIVATE + msgResultAPI.getPublisherUuid())
                                .execute();
                    } else {                                                //update unsuccessful
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, false, "PersonalInfo"))
                                .channel(Channels.PRIVATE + msgResultAPI.getPublisherUuid())
                                .execute();
                    }
                } else if (msgResultAPI.getChannel().equals(Channels.UPDATE_PASSWORD.toString())) {     //Checking if updating password
                    if (DBManager.getInstance().updatePassword(data)) {                                 //checking if password is updated
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, true, "Password"))
                                .channel(Channels.PRIVATE + msgResultAPI.getPublisherUuid())
                                .execute();
                    } else {                                                //update unsuccessful
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, false, "Password"))
                                .channel(Channels.PRIVATE + msgResultAPI.getPublisherUuid())
                                .execute();
                    }
                } else if (msgResultAPI.getChannel().equals(Channels.UPDATE_DELETE.toString())) {
                    if (DBManager.getInstance().updateIsDeleted(data)) {
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, true, "Delete"))
                                .channel(Channels.PRIVATE + msgResultAPI.getPublisherUuid())
                                .execute();
                    }
                    else {
                        messagingAPI.publish()
                                .message(new UpdateResponseData(data, false, "Delete"))
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
