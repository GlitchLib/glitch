package glitch.chat;

import glitch.chat.events.RawIRCEvent;
import glitch.chat.events.RawIRCEventImpl;
import glitch.chat.irc.IRCPrefix;
import glitch.chat.irc.Tags;
import glitch.socket.events.RawMessageEvent;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class MessageParser {
    static RawIRCEvent parseMessage(RawMessageEvent<GlitchChat> event) {
        return parseMessage(event.getMessage(), event.getClient());
    }

    private static RawIRCEvent parseMessage(String raw, GlitchChat chat) {
        if (raw.contains(System.lineSeparator())) {
            for (String l : raw.split(System.lineSeparator())) {
                return parseMessage(l, chat);
            }
        }

        RawIRCEventImpl.Builder irc = RawIRCEventImpl.builder().rawMessage(raw);

        boolean lockTrailing = false;

        for (String part : raw.split(" ")) {
            if (part.startsWith("@")) {
                Map<String, String> tags = new LinkedHashMap<>();
                Arrays.stream(part.substring(1).split(";"))
                        .forEach(tag -> {
                            final String[] entry = tag.split("=", 2);
                            final String key = entry[0];
                            String value = entry[1];
                            if (value.contains("\\r")) value = value.replace("\\r", "\r");
                            if (value.contains("\\n")) value = value.replace("\\n", "\n");
                            if (value.contains("\\\\")) value = value.replace("\\\\", "\\");
                            if (value.contains("\\s")) value = value.replace("\\s", " ");
                            if (value.contains("\\:")) value = value.replace("\\:", ":");

                            if (key != null && !key.equals("")) {
                                tags.put(key, value);
                                if (key.equals("tmi-sent-ts"))
                                    irc.createdAt(new Date(Long.parseLong(value)).toInstant());
                            }
                        });
                irc.tags(Tags.of(tags));
            } else if (part.startsWith(":")) {
                if (part.matches("^:(.+)(!.+)*?(@.+)*?$")) {
                    irc.prefix(IRCPrefix.fromRaw(part));
                } else {
                    lockTrailing = true;
                    irc.trailing(raw.substring(raw.indexOf(part) + 1));
                }
            } else if (part.matches("^([A-Z]+|[0-9]{1,3})")) {
                irc.command(parseCommand(part));
            } else {
                if (!lockTrailing)
                    irc.addMiddle(part);
            }
        }

        return irc.client(chat).build();
    }

    private static IRCCommand parseCommand(String cmd) {
        switch (cmd) {
            case "PRIVMSG":
                return IRCCommand.PRIV_MSG;
            case "NOTICE":
                return IRCCommand.NOTICE;
            case "PING":
                return IRCCommand.PING;
            case "PONG":
                return IRCCommand.PONG;
            case "HOSTTARGET":
                return IRCCommand.HOST_TARGET;
            case "CLEARCHAT":
                return IRCCommand.CLEAR_CHAT;
            case "USERSTATE":
                return IRCCommand.USER_STATE;
            case "GLOBALUSERSTATE":
                return IRCCommand.GLOBAL_USER_STATE;
            case "NICK":
                return IRCCommand.NICK;
            case "JOIN":
                return IRCCommand.JOIN;
            case "PART":
                return IRCCommand.PART;
            case "PASS":
                return IRCCommand.PASS;
            case "CAP":
                return IRCCommand.CAP;
            case "001":
                return IRCCommand.RPL_WELCOME;
            case "002":
                return IRCCommand.RPL_YOURHOST;
            case "003":
                return IRCCommand.RPL_CREATED;
            case "004":
                return IRCCommand.RPL_MYINFO;
            case "353":
                return IRCCommand.RPL_NAMREPLY;
            case "366":
                return IRCCommand.RPL_ENDOFNAMES;
            case "372":
                return IRCCommand.RPL_MOTD;
            case "375":
                return IRCCommand.RPL_MOTDSTART;
            case "376":
                return IRCCommand.RPL_ENDOFMOTD;
            case "421":
                return IRCCommand.ERR_UNKNOWNCOMMAND;
            case "WHISPER":
                return IRCCommand.WHISPER;
            case "SERVERCHANGE":
                return IRCCommand.SERVER_CHANGE;
            case "RECONNECT":
                return IRCCommand.RECONNECT;
            case "ROOMSTATE":
                return IRCCommand.ROOM_STATE;
            case "USERNOTICE":
                return IRCCommand.USER_NOTICE;
            default:
                IRCCommand com = IRCCommand.UNKNOWN;
                com.value = cmd;
                return com;
        }
    }
}
