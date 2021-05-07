package io.github.gameengine.proj;


import io.github.API.ISubscribeCallback;
import io.github.API.MessagingAPI;
import io.github.API.messagedata.MsgResultAPI;
import io.github.API.messagedata.MsgStatus;
import io.github.API.utils.GsonWrapper;
import io.github.coreutils.proj.messages.Channels;
import io.github.coreutils.proj.messages.LoginData;
import io.github.coreutils.proj.messages.LoginResponseData;

public class AuthorizationCallback extends DBSource implements ISubscribeCallback{

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

                    String userName = "utsavp";
                    String firstName = "utsav";
                    String lastName = "parajuli";
                    String password = "heyo";

                    System.out.println("1");
                    //trying to add to the database
                    String sql = "INSERT INTO users(username, firstname, lastname, password) VALUES(?, ?, ?, ?);";
                    boolean result = false;

                    System.out.println("2");

                    System.out.println(getDataSource().getConnection().toString());
//                    try (
//                            Connection connection = getDataSource().getConnection();
//
//                            PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql, userName, firstName,
//                                    lastName, password) {
//                                @Override
//                                protected void prepareStatement(Object... params) throws SQLException {
//                                    stat.setString(1, (String) params[0]);
//                                    stat.setString(2, (String) params[1]);
//                                    stat.setString(3, (String) params[2]);
//                                    stat.setString(4, (String) params[3]);
//                                }
//                            };
//                    ) {
                    //stat.executeUpdate() != 0
                        if (false) {     // Todo: if doesnt already exists { DB call }
                            // TODO: added user data to database { DB call }
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
//                } catch (Exception e) {
//                        e.printStackTrace();
//                        mApi.publish()
//                                .message(new LoginResponseData(data, false, "there was an error"))
//                                .channel(Channels.PRIVATE + message.getPublisherUuid())
//                                .execute();
//                    }
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
