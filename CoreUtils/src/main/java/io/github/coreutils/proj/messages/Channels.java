package io.github.coreutils.proj.messages;

public enum Channels {
    // authorization service messages
    AUTHORIZATION("Authorization::"),
    AUTHOR_CREATE(AUTHORIZATION.value + "Create"),
    AUTHOR_VALIDATE(AUTHORIZATION.value + "Validate"),

    // update account messages
    UPDATE("Update::"),
    UPDATE_USERNAME(UPDATE.value + "Username"),
    UPDATE_PERSONAL_INFO(UPDATE.value + "Personal Info"),
    UPDATE_PASSWORD(UPDATE.value + "Password"),
    UPDATE_DELETE(UPDATE.value + "Delete"),

    //game engine messages
    ROOM("Room::"),
    ROOM_REQUEST(ROOM.value + "Requests"),
    ROOM_LIST(ROOM.value + "List"),
    ROOM_MOVE(ROOM.value + "Move"),

    // BY GRANT GOLDSWORTH
    GET_DATA("Data::"),
    GET_ROOMS_DATA(GET_DATA.value + "Rooms"),
    GET_MOVES_DATA(GET_DATA.value + "Moves"),

    // request back channel
    REQUEST("REQUEST::"),
    REQUEST_MOVE(REQUEST.value + "Move"),

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
