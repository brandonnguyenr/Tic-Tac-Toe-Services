package io.github.gameengine.proj;

import io.github.API.MessagingAPI;
import io.github.library.proj.messages.Channels;

import java.io.IOException;

public class GameEngine {
    private MessagingAPI api;

    public GameEngine() {
        try {
            api = new MessagingAPI();

            api.subscribe()
                    .channels(Channels.AUTHORIZATION.toString(), Channels.ROOM_MOVE.toString(), Channels.ROOM.toString())
                    .execute();

        } catch (IOException e) {
            api.free();
            System.out.println(e.getMessage());
        }
    }
}
