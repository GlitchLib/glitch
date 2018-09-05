package glitch.chat;

import glitch.chat.events.RawIRCEvent;
import glitch.chat.events.RawIRCEventBuilder;
import glitch.chat.irc.IRCPrefix;
import glitch.chat.irc.IRCPrefixBuilder;
import java.time.Instant;
import java.util.Date;
import java.util.StringTokenizer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class MessageParser {
    static RawIRCEvent parseMessage(String raw) {
        if (raw.contains(System.lineSeparator())) {
            for (String l : raw.split(System.lineSeparator())) {
                return parseMessage(l);
            }
        }

        RawIRCEventBuilder irc = new RawIRCEventBuilder();

        boolean lockTrailing = false;

        for (String part: raw.split(" ")) {
            if (part.startsWith("@")) {
                StringTokenizer tagsTokenizer = new StringTokenizer(part.substring(1), ";");
                while (tagsTokenizer.hasMoreElements()) {
                    final String[] entry = tagsTokenizer.nextToken().split("=", 2);
                    final String key = entry[0];
                    String value = entry[1];
                    if (value.contains("\\r")) value = value.replace("\\r", "\r");
                    if (value.contains("\\n")) value = value.replace("\\n", "\n");
                    if (value.contains("\\\\")) value = value.replace("\\\\", "\\");
                    if (value.contains("\\s")) value = value.replace("\\s", " ");
                    if (value.contains("\\:")) value = value.replace("\\:", ";");

                    switch (key) {
                        case "":
                            break;
                        default:
                            irc.putTags(key, value);
                            if (key.equals("tmi-sent-ts")) irc.createdAt(new Date(Long.parseLong(value)).toInstant());
                            break;
                    }
                }
            } else if (part.startsWith(":")) {
                if (part.matches("^:(.+)!(.+)@(.+)$")) {
                    irc.prefix(parseRawPrefix(part.substring(1)));
                } else {
                    lockTrailing = true;
                    irc.addMiddle(part);
                }
            }  else if (part.matches("^([A-Z]+|[0-9]{1,3})")) {
                irc.command(parseCommand(part));
            } else {
                if (lockTrailing)
                    irc.addMiddle(part);
                else
                    irc.addTrailing(part);
            }
        }

        return irc.build();
    }
    private static IRCPrefix parseRawPrefix(String rawPrefix) {
        IRCPrefixBuilder prefix = new IRCPrefixBuilder().raw(":" + rawPrefix);
        if (rawPrefix.contains("@")) {
            String[] nh = rawPrefix.split("@");
            prefix.host(nh[1]);
            if (nh[0].contains("!")) {
                String[] nu  = nh[0].split("!");
                prefix.nick(nu[0])
                        .user(nu[1]);
            } else {
                prefix.nick(nh[0]);
            }
        } else {
            prefix.host(rawPrefix);
        }

        return prefix.build();
    }
    private static IRCommand parseCommand(String cmd) {
        switch (cmd) {
            case "PRIVMSG":
                return IRCommand.PRIV_MSG;
            case "NOTICE":
                return IRCommand.NOTICE;
            case "PING":
                return IRCommand.PING;
            case "PONG":
                return IRCommand.PONG;
            case "HOSTTARGET":
                return IRCommand.HOST_TARGET;
            case "CLEARCHAT":
                return IRCommand.CLEAR_CHAT;
            case "USERSTATE":
                return IRCommand.USER_STATE;
            case "GLOBALUSERSTATE":
                return IRCommand.GLOBAL_USER_STATE;
            case "NICK":
                return IRCommand.NICK;
            case "JOIN":
                return IRCommand.JOIN;
            case "PART":
                return IRCommand.PART;
            case "PASS":
                return IRCommand.PASS;
            case "CAP":
                return IRCommand.CAP;
            case "001":
                return IRCommand.RPL_001;
            case "002":
                return IRCommand.RPL_002;
            case "003":
                return IRCommand.RPL_003;
            case "004":
                return IRCommand.RPL_004;
            case "353":
                return IRCommand.RPL_353;
            case "366":
                return IRCommand.RPL_366;
            case "372":
                return IRCommand.RPL_372;
            case "375":
                return IRCommand.RPL_375;
            case "376":
                return IRCommand.RPL_376;
            case "WHISPER":
                return IRCommand.WHISPER;
            case "SERVERCHANGE":
                return IRCommand.SERVER_CHANGE;
            case "RECONNECT":
                return IRCommand.RECONNECT;
            case "ROOMSTATE":
                return IRCommand.ROOM_STATE;
            case "USERNOTICE":
                return IRCommand.USER_NOTICE;
            default:
                IRCommand com = IRCommand.UNKNOWN;
                com.value = cmd;
                return com;
        }
    }
}
