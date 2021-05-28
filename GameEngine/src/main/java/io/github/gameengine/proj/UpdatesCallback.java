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
    public void status(MessagingAPI mApi, MsgStatus status) {
    }

    /**
     * This method will get the data from player and make calls to the database and publish the result
     * back to the particular api call.
     * @param mApi: api
     * @param message: api containing the result
     */
    @Override
    public void resolved(MessagingAPI mApi, MsgResultAPI message) {
        if (message.getChannel().equals(Channels.UPDATE_USERNAME.toString()) ||
                message.getChannel().equals(Channels.UPDATE_PERSONAL_INFO.toString()) ||
                message.getChannel().equals(Channels.UPDATE_PASSWORD.toString()) ||
                message.getChannel().equals(Channels.UPDATE_DELETE.toString())) {

            UpdateData data = GsonWrapper.fromJson(message.getMessage(), UpdateData.class);

            try {
                if (message.getChannel().equals(Channels.UPDATE_USERNAME.toString())) {     //Checking if updating username
                    if (DBManager.getInstance().updateUsername(data)) {                          //checking if username updated
                        mApi.publish()
                                .message(new UpdateResponseData(data, true, "Username"))
                                .channel(Channels.PRIVATE + message.getPublisherUuid())
                                .execute();
                    } else {                                                //update unsuccessful
                        mApi.publish()
                                .message(new UpdateResponseData(data, false, "Username"))
                                .channel(Channels.PRIVATE + message.getPublisherUuid())
                                .execute();
                    }
                } else if (message.getChannel().equals(Channels.UPDATE_PERSONAL_INFO.toString())) {//Checking if updating info
                    if (DBManager.getInstance().updatePersonalInfo(data)) {                             //checking if info updated
                        mApi.publish()
                                .message(new UpdateResponseData(data, true, "PersonalInfo"))
                                .channel(Channels.PRIVATE + message.getPublisherUuid())
                                .execute();
                    } else {                                                //update unsuccessful
                        mApi.publish()
                                .message(new UpdateResponseData(data, false, "PersonalInfo"))
                                .channel(Channels.PRIVATE + message.getPublisherUuid())
                                .execute();
                    }
                } else if (message.getChannel().equals(Channels.UPDATE_PASSWORD.toString())) {     //Checking if updating password
                    if (DBManager.getInstance().updatePassword(data)) {                                 //checking if password is updated
                        mApi.publish()
                                .message(new UpdateResponseData(data, true, "Password"))
                                .channel(Channels.PRIVATE + message.getPublisherUuid())
                                .execute();
                    } else {                                                //update unsuccessful
                        mApi.publish()
                                .message(new UpdateResponseData(data, false, "Password"))
                                .channel(Channels.PRIVATE + message.getPublisherUuid())
                                .execute();
                    }
                } else if (message.getChannel().equals(Channels.UPDATE_DELETE.toString())) {       //checking if updating deletion status
                    if (DBManager.getInstance().updateIsDeleted(data)) {
                        //checking if deletion was successful
                        mApi.publish()
                                .message(new UpdateResponseData(data, true, "Delete"))
                                .channel(Channels.PRIVATE + message.getPublisherUuid())
                                .execute();
                        // updates online list in lobby page
                        mApi.publish()
                                .message(new OnlineState(data.getUsername(), false))
                                .channel(Channels.OFFLINE_STATE.toString())
                                .execute();
                    }
                    else {
                        mApi.publish()
                                .message(new UpdateResponseData(data, false, "Delete"))
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
