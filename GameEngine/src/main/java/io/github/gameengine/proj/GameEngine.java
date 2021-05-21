package io.github.gameengine.proj;

import io.github.API.MessagingAPI;
import io.github.coreutils.proj.messages.Channels;
import io.github.coreutils.proj.messages.RoomData;
import io.github.gameengine.proj.enginedata.Lobby;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class GameEngine {
    private final List<RoomData> roomList = Collections.synchronizedList(new ArrayList<>());
    private final Map<Integer, Lobby> lobbyDir = Collections.synchronizedMap(new HashMap<>());
    private MessagingAPI api;
    private AuthorizationCallback ac;
    private MoveCallback mc;
    private RoomsCallback rc;
    private GamesCallback gc;
    private UpdatesCallback uc;
    private static int gameID = 1;
    private final CountDownLatch latch;

    public GameEngine() {
        latch = new CountDownLatch(1);
        Scanner in = new Scanner(System.in);
        api = new MessagingAPI();
        ac = new AuthorizationCallback();
        uc = new UpdatesCallback();
        rc = new RoomsCallback(roomList);
        gc = new GamesCallback(roomList, lobbyDir);
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
        api.addEventListener(rc, Channels.ROOM_LIST.toString());
        api.addEventListener(gc, Channels.ROOM_REQUEST.toString(), Channels.ROOM_MOVE.toString());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (api.isAlive()) {
                System.out.println("cleaned externally");
                api.free();
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
//        Signal.handle(new Signal("INT"), sig -> {
//            api.free();
//            System.out.println("closed");
//        });

        api.onclose(() -> {
            System.out.println("api is now dead..");
            latch.countDown();
        });

        if (in.next().equalsIgnoreCase("KILL"))
            api.free();
    }

    public static void main(String[] args) {
        new GameEngine();
    }

    public static int getGameID() {
        return gameID++;
    }
}