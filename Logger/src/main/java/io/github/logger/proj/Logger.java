package io.github.logger.proj;

import io.github.API.MessagingAPI;
import io.github.library.proj.messages.Channels;

import java.io.IOException;

public class Logger {
    private MessagingAPI api;
    private LoggerCallback callback;

    public Logger() {
        try {
            api = new MessagingAPI();
            callback = new LoggerCallback();

            api.subscribe().channels(Channels.ROOM_MOVE.toString(), Channels.ROOM.toString()).execute();
            api.addEventListener(callback, Channels.ROOM_MOVE.toString(), Channels.ROOM.toString());
        } catch (IOException e) {
            api.free();
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Logger();
    }
}
