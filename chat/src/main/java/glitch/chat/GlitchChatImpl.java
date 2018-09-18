package glitch.chat;

import glitch.GlitchClient;
import glitch.chat.events.RawIRCEvent;
import glitch.socket.GlitchWebSocketImpl;
import glitch.socket.events.actions.OpenEvent;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class GlitchChatImpl extends GlitchWebSocketImpl implements GlitchChat {
    private final BotConfig botConfig;

    private final Set<String> channels = new LinkedHashSet<>();

    private final Logger logger = LoggerFactory.getLogger("glitch.tmi.chat");

    @SuppressWarnings("unchecked")
    GlitchChatImpl(GlitchClient client, BotConfig botConfig, Collection<String> channels) {
        super(client, "wss://irc-ws.chat.twitch.tv:443");
        this.botConfig = botConfig;
        this.ping.set("PING :tmi.twitch.tv");
        this.pong.set("PONG :tmi.twitch.tv");

        if (!channels.isEmpty()) {
            this.channels.addAll(channels);
        }

        listenOn(RawIRCEvent.class)
                .doOnEach(eventNotification -> {
                    if (eventNotification.isOnError()) {
                        logger.error(eventNotification.getError().getMessage(), eventNotification.getError());
                    }
                    if (eventNotification.isOnNext()) {
                        RawIRCEvent event = eventNotification.getValue();
                        StringBuilder sb = new StringBuilder();

                        logger.debug("Received RawMessage({})", event.getRawMessage());
                    }
                }).subscribe(new IRCMessageConsumer(this));
        listenOn(OpenEvent.class).subscribe(event -> {
            send("CAP REQ :twitch.tv/commands");
            send("CAP REQ :twitch.tv/membership");
            send("CAP REQ :twitch.tv/tags");

            send("PASS " + botConfig.getPassword());
            send("NICK " + botConfig.getUsername());

            if (!channels.isEmpty()) {
                channels.forEach(ch -> send("JOIN " + ch));
            }

            // Attempt self join to channel to handling private messages
            if (!channels.contains(botConfig.getUsername())) {
                joinChannel(botConfig.getUsername());
            }
        });
    }

    @Override
    public void onMessage(String message) {
        dispatcher.onNext(MessageParser.parseMessage(message, this));
    }

    @Override
    public void joinChannel(String channel) {
        if (isOpen()) {
            send("JOIN " + channel);
            channels.add(channel);
        }
    }

    @Override
    public void partChannel(String channel) {
        if (isOpen()) {
            send("PART " + channel);
            channels.remove(channel);
        }
    }

    @Override
    public void sendMessage(String channel, String message) {
        if (channel.contains(channel)) {
            send(String.format("PRIVMSG #%s :%s", channel, message));
        } // TODO: throw exception if you not joined to the channel
    }

    @Override
    public void sendPrivateMessage(String username, String message) {
        sendMessage(botConfig.getUsername(), String.format("/w %s %s", username, message));
    }
}
