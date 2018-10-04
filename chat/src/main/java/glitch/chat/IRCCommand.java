package glitch.chat;

public enum IRCCommand {
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
    RPL_WELCOME,
    RPL_YOURHOST,
    RPL_CREATED,
    RPL_MYINFO,
    RPL_NAMREPLY,
    RPL_ENDOFNAMES,
    RPL_MOTD,
    RPL_MOTDSTART,
    RPL_ENDOFMOTD,
    ERR_UNKNOWNCOMMAND,
    WHISPER,
    ROOM_STATE,
    RECONNECT,
    SERVER_CHANGE,
    USER_NOTICE;

    String value;

    IRCCommand() {
        this.value = (name().startsWith("RPL_")) ? name().substring(4) : name();
    }

    @Override
    public String toString() {
        return value;
    }
}
