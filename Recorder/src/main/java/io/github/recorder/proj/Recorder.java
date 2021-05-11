package io.github.recorder.proj;

import io.github.API.MessagingAPI;
import io.github.coreutils.proj.messages.Channels;

public class Recorder {
    private MessagingAPI api;
    private RecorderCallback callback;

    public Recorder() {
        api = new MessagingAPI();
        callback = new RecorderCallback();

        api.subscribe().channels(Channels.ROOM_MOVE.toString(), Channels.ROOM.toString()).execute();
        api.addEventListener(callback, Channels.ROOM_MOVE.toString(), Channels.ROOM.toString());
        api.onclose(() -> {
            System.out.println("api is now dead..");
        });
    }

    public static void main(String[] args) {
        new Recorder();
    }
}
