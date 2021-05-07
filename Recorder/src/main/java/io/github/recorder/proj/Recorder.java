package io.github.recorder.proj;

import io.github.API.MessagingAPI;
import io.github.coreutils.proj.messages.Channels;

import java.io.IOException;

public class Recorder {
    private MessagingAPI api;
    private RecorderCallback callback;

    public Recorder() {
        try {
            api = new MessagingAPI();
            callback = new RecorderCallback();

            api.subscribe().channels(Channels.ROOM_MOVE.toString(), Channels.ROOM.toString()).execute();
            api.addEventListener(callback, Channels.ROOM_MOVE.toString(), Channels.ROOM.toString());
        } catch (IOException e) {
            api.free();
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Recorder();
    }
}
