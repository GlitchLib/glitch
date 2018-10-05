package glitch.chat;

import glitch.GlitchClient;
import glitch.chat.api.ChatAPI;
import glitch.chat.events.RawIRCEvent;
import glitch.socket.WebSocketImpl;
import glitch.socket.events.RawMessageEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ChatImpl extends WebSocketImpl implements GlitchChat {
    private final ChatAPI api;
    private final BotConfig config;

    public ChatImpl(GlitchClient client, BotConfig config, ChatAPI api) {
        super(client, "wss://irc-ws.chat.twitch.tv:443");
        this.config = config;
        this.api = api;

        registerListeners();
    }

    @Override
    public void joinChannel(String channel) {
        // TODO: Move to IRC Cache
    }

    @Override
    public void partChannel(String channel) {
        // TODO: Move to IRC Cache
    }

    @Override
    public void sendMessage(String channel, String message) {
        // TODO: Move to IRC Cache
    }

    @Override
    public void sendPrivateMessage(String username, String message) {
        // TODO: Move to IRC Cache
    }

    @SuppressWarnings("unchecked")
    private void registerListeners() {
        listenOn(RawIrcMessageEvent.class).doOnEach(eventNotification -> {
            if (eventNotification.isOnError()) {
                log.error(eventNotification.getError().getMessage(), eventNotification.getError());
            }
            if (eventNotification.isOnNext()) {
                RawMessageEvent<GlitchChat> event = eventNotification.getValue();

                log.debug("Received RawMessage({})", event.getMessage());
            }
        }).subscribe(event -> getDispatcher().onNext(MessageParser.parseMessage(event)));

        listenOn(RawIRCEvent.class).subscribe(IRConsumer::composeFrom);
    }

    private interface RawIrcMessageEvent extends RawMessageEvent<GlitchChat> {
    }
}
