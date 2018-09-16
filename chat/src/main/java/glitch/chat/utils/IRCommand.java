package glitch.chat.utils;

public enum IRCommand {
    UNKNOWN,
    PRIV_MSG,
    NOTICE,
    PING,
    PONG,
    JOIN,
    PART,
    HOST_TARGET,
    CLEAR_CHAT,
    USER_STATE,
    GLOBAL_USER_STATE,
    NICK,
    PASS,
    CAP,
    RPL_001,
    RPL_002,
    RPL_003,
    RPL_004,
    RPL_353,
    RPL_366,
    RPL_372,
    RPL_375,
    RPL_376,
    WHISPER,
    ROOM_STATE,
    RECONNECT,
    SERVER_CHANGE,
    USER_NOTICE;

    String value;

    IRCommand() {
        this.value = (name().startsWith("RPL_")) ? name().substring(4) : name();
    }

    @Override
    public String toString() {
        return value;
    }
}
