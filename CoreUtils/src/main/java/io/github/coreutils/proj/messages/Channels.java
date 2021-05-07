package io.github.coreutils.proj.messages;

public enum Channels {
    // authorization service messages
    AUTHORIZATION("Authorization::"),
    AUTHOR_CREATE(AUTHORIZATION.value + "Create"),
    AUTHOR_VALIDATE(AUTHORIZATION.value + "Validate"),

    //game engine messages
    ROOM("Room::"),
    ROOM_REQUEST(ROOM.value + "Requests"),
    ROOM_LIST(ROOM.value + "List"),
    ROOM_MOVE(ROOM.value + "Move"),

    //private messages
    PRIVATE("Private::");

    private final String value;

    Channels(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
