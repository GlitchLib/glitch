package io.glitchlib.tmi.irc

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 0.1.0
 */
enum class Command {
    UNKNOWN,
    PRIV_MSG,
    NOTICE,
    PING,
    PONG,
    JOIN,
    PART,
    HOST_TARGET,
    CLEAR_CHAT,
    CLEAR_MESSAGE,
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

    internal var value = if (name.startsWith("RPL_")) name.substring(4) else name

    override fun toString(): String {
        return value
    }

    companion object {

        fun of(cmd: String) =
                when (cmd) {
                    "PRIVMSG" -> PRIV_MSG
                    "NOTICE" -> NOTICE
                    "PING" -> PING
                    "PONG" -> PONG
                    "HOSTTARGET" -> HOST_TARGET
                    "CLEARCHAT" -> CLEAR_CHAT
                    "USERSTATE" -> USER_STATE
                    "GLOBALUSERSTATE" -> GLOBAL_USER_STATE
                    "NICK" -> NICK
                    "JOIN" -> JOIN
                    "PART" -> PART
                    "PASS" -> PASS
                    "CAP" -> CAP
                    "001" -> RPL_WELCOME
                    "002" -> RPL_YOURHOST
                    "003" -> RPL_CREATED
                    "004" -> RPL_MYINFO
                    "353" -> RPL_NAMREPLY
                    "366" -> RPL_ENDOFNAMES
                    "372" -> RPL_MOTD
                    "375" -> RPL_MOTDSTART
                    "376" -> RPL_ENDOFMOTD
                    "421" -> ERR_UNKNOWNCOMMAND
                    "WHISPER" -> WHISPER
                    "SERVERCHANGE" -> SERVER_CHANGE
                    "RECONNECT" -> RECONNECT
                    "ROOMSTATE" -> ROOM_STATE
                    "USERNOTICE" -> USER_NOTICE
                    "CLEARMSG" -> CLEAR_MESSAGE
                    else -> UNKNOWN.apply { value = cmd }
                }
    }
}