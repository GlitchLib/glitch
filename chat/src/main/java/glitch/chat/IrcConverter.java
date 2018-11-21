package glitch.chat;

import glitch.api.ws.Converter;
import glitch.api.ws.events.IEvent;
import glitch.chat.events.ChannelMessageEvent;
import glitch.chat.events.JoinUserChannelEvent;
import glitch.chat.events.PartUserChannelEvent;
import glitch.chat.events.RawIrcEvent;
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

        Message.Builder rawMsg = Message.builder().rawMessage(raw);

        boolean trailingLocked = false;

        for (String part : raw.split(" ")) {
            if (part.startsWith("@") && !trailingLocked) {
                Map<String, String> tags = new LinkedHashMap<>();
                Arrays.stream(part.substring(1).split(";"))
                        .map(tag -> tag.split("=", 2))
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
            } else if (part.startsWith(":")) {
                if (part.matches("^:(.+)(!.+)*?(@.+)*?$")) {
                    rawMsg.prefix(Prefix.fromRaw(part));
                } else {
                    trailingLocked = true;
                    rawMsg.trailing(raw.substring(raw.indexOf(part) + 1));
                }
            } else if (part.matches("^([A-Z]+|[0-9]{1,3})")) {
                rawMsg.command(Command.of(part));
            } else {
                if (!trailingLocked)
                    rawMsg.middle(part);
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
            case UNKNOWN:
            default:
                return new RawIrcEvent(chat, message);
        }
    }
}
