package io.github.gameengine.proj;

import io.github.API.MessagingAPI;
import io.github.coreutils.proj.messages.Channels;

public class GameEngine {
    private MessagingAPI api;
    private AuthorizationCallback ac;
    private MoveCallback mc;
    private RoomsCallback rc;
    private GamesCallback gc;
    private UpdatesCallback uc;
    private static int gameID = 1;

    public GameEngine() {
        api = new MessagingAPI();
        ac = new AuthorizationCallback();
        uc = new UpdatesCallback();
        api.subscribe()
                .channels(Channels.AUTHOR_VALIDATE.toString(),
                        Channels.AUTHOR_CREATE.toString(),
                        Channels.UPDATE_USERNAME.toString(),
                        Channels.UPDATE_PERSONAL_INFO.toString(),
                        Channels.UPDATE_PASSWORD.toString(),
                        Channels.ROOM_REQUEST.toString(),
                        Channels.ROOM_MOVE.toString(),
                        Channels.ROOM_LIST.toString())
                .execute();

        api.addEventListener(ac, Channels.AUTHOR_VALIDATE.toString(), Channels.AUTHOR_CREATE.toString());

        api.addEventListener(uc, Channels.UPDATE_USERNAME.toString(), Channels.UPDATE_PERSONAL_INFO.toString(),
                Channels.UPDATE_PASSWORD.toString());

        api.onclose(() -> {
            System.out.println("api is now dead..");
        });
    }

    public static void main(String[] args) {
        new GameEngine();
    }

    public static int getGameID() {
        return gameID++;
    }
}