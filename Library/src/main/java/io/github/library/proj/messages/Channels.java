package io.github.library.proj.messages;

public enum Channels {
    // authorization service
    AUTHORIZATION("Authorization"),
    CREATEACCOUNT("CreateAccount"),
    DELETEACCOUNT("DeleteAccount"),
    UPDATEACCOUNT("UpdateAccount"),

    //game engine
    ROOM("Room"),
    ROOM_MOVE("Room_Moves");

    private final String value;

    Channels(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
