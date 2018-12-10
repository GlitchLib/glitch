package glitch.chat;

import glitch.api.ws.Converter;
import glitch.api.ws.events.IEvent;
import glitch.api.ws.events.PingEvent;
import glitch.api.ws.events.PongEvent;
import glitch.chat.events.*;
import glitch.chat.object.irc.Command;
import glitch.chat.object.irc.Message;
import glitch.chat.object.irc.Prefix;
import glitch.chat.object.irc.Tags;
import okio.ByteString;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class IrcConverter implements Converter<GlitchChat> {
    @Override
    public IEvent<GlitchChat> convert(ByteString value, GlitchChat chat) {
        Message message = toMessage(value.utf8());
        return doConvert(message, chat);
    }

    private Message toMessage(String raw) {
        if (raw.contains(System.lineSeparator())) {
            for (String l : raw.split(System.lineSeparator())) {
                return toMessage(l);
            }
        }
        String[] splitRawMsg = Arrays.copyOf(raw.split(" :", 3), 3);
        Message.Builder rawMsg = Message.builder().rawMessage(raw);

        if (splitRawMsg[0].startsWith("@")) {
            String rawTag = splitRawMsg[0].substring(1, splitRawMsg[0].indexOf(" "));
            Map<String, String> tags = new LinkedHashMap<>();
            Arrays.stream(rawTag.split(";"))
                    .map(tag -> Arrays.copyOf(tag.split("=", 2), 2))
                    .map(entry -> new AbstractMap.SimpleImmutableEntry<>(entry[0], entry[1]))
                    .forEach(tag -> {
                        String value = tag.getValue();
                        if (value.contains("\\r")) value = value.replace("\\r", "\r");
                        if (value.contains("\\n")) value = value.replace("\\n", "\n");
                        if (value.contains("\\\\")) value = value.replace("\\\\", "\\");
                        if (value.contains("\\s")) value = value.replace("\\s", " ");
                        if (value.contains("\\:")) value = value.replace("\\:", ":");

                        if (tag.getKey() != null && !tag.getKey().equals("")) {
                            tags.put(tag.getKey(), value);
                        }
                    });
            rawMsg.tags(Tags.of(tags));

            splitRawMsg[0] = splitRawMsg[1].trim();

            if (splitRawMsg.length > 2) {
                splitRawMsg[1] = splitRawMsg[2].trim();
            }
        }

        rawMsg.trailing(splitRawMsg[1]);

        for (String part : splitRawMsg[0].split(" ", 3)) {
            if (part.matches("^(.+)(!.+)*?(@.+)*?$")) {
                rawMsg.prefix(Prefix.fromRaw(part));
            } else if (part.matches("^([A-Z]+|[0-9]{1,3})")) {
                rawMsg.command(Command.of(part));
            } else {
                rawMsg.middle(Arrays.asList(part.split(" ")));
            }
        }

        return rawMsg.build();
    }

    private IEvent<GlitchChat> doConvert(Message message, GlitchChat chat) {
        switch (message.getCommand()) {
            case JOIN:
                return new JoinUserChannelEvent(chat, message);
            case PART:
                return new PartUserChannelEvent(chat, message);
            case PRIV_MSG:
                return new ChannelMessageEvent(chat, message);
            case CLEAR_CHAT:
                if (message.getTrailing() != null) {
                    if (message.getTags().containsKey("ban-duration")) {
                        return new ChannelTimeoutEvent(chat, message);
                    } else {
                        return new ChannelBanEvent(chat, message);
                    }
                } else {
                    return new ChannelClearChatEvent(chat, message);
                }
            case CLEAR_MESSAGE:
                return new ChannelDeleteMessageEvent(chat, message);
            case PING:
                return new PingEvent<>(chat);
            case PONG:
                return new PongEvent<>(chat);
            case GLOBAL_USER_STATE:
                return new GlobalUserStateEvent(chat, message);
            case NOTICE:
                return new ChannelNoticeEvent(chat, message);
            case ROOM_STATE:
                if (message.getTags().size() > 1) {
                    return new ChannelStateEvent(chat, message);
                } else {
                    return new ChannelStateChangedEvent(chat, message);
                }
            case USER_NOTICE:
                return doUserNotice(chat, message);
            case USER_STATE:
                return new ChannelUserStateEvent(chat, message);
            default:
                return new RawIrcEvent(chat, message);
        }
    }

    private IEvent<GlitchChat> doUserNotice(GlitchChat chat, Message message) {
        switch (message.getTags().get("msg-id")) {
            case "sub":
                return new ChannelSubscriptionEvent(chat, message);
            case "resub":
                return new ChannelResubEvent(chat, message);
            case "raid":
                return new ChannelRaidEvent(chat, message);
            case "ritual":
                return new ChannelNewChatterEvent(chat, message);
            default:
                return new RawIrcEvent(chat, message);
        }
    }
}
