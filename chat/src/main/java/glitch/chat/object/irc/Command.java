package glitch.chat.object.irc;

public enum Command {
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

    private String value;

    Command() {
        this.value = (name().startsWith("RPL_")) ? name().substring(4) : name();
    }

    @Override
    public String toString() {
        return value;
    }



    public static Command of(String cmd) {
        switch (cmd) {
            case "PRIVMSG":
                return PRIV_MSG;
            case "NOTICE":
                return NOTICE;
            case "PING":
                return PING;
            case "PONG":
                return PONG;
            case "HOSTTARGET":
                return HOST_TARGET;
            case "CLEARCHAT":
                return CLEAR_CHAT;
            case "USERSTATE":
                return USER_STATE;
            case "GLOBALUSERSTATE":
                return GLOBAL_USER_STATE;
            case "NICK":
                return NICK;
            case "JOIN":
                return JOIN;
            case "PART":
                return PART;
            case "PASS":
                return PASS;
            case "CAP":
                return CAP;
            case "001":
                return RPL_WELCOME;
            case "002":
                return RPL_YOURHOST;
            case "003":
                return RPL_CREATED;
            case "004":
                return RPL_MYINFO;
            case "353":
                return RPL_NAMREPLY;
            case "366":
                return RPL_ENDOFNAMES;
            case "372":
                return RPL_MOTD;
            case "375":
                return RPL_MOTDSTART;
            case "376":
                return RPL_ENDOFMOTD;
            case "421":
                return ERR_UNKNOWNCOMMAND;
            case "WHISPER":
                return WHISPER;
            case "SERVERCHANGE":
                return SERVER_CHANGE;
            case "RECONNECT":
                return RECONNECT;
            case "ROOMSTATE":
                return ROOM_STATE;
            case "USERNOTICE":
                return USER_NOTICE;
            default:
                Command com = UNKNOWN;
                com.value = cmd;
                return com;
        }
    }
}