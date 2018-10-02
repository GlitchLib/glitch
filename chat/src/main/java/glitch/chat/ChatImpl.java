package glitch.chat;

import glitch.GlitchClient;
import glitch.chat.events.RawIRCEvent;
import glitch.socket.WebSocketImpl;
import glitch.socket.events.message.RawMessageEvent;
import io.reactivex.Notification;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatImpl extends WebSocketImpl implements GlitchChat {
    private final BotConfig config;

    public ChatImpl(GlitchClient client, BotConfig config) {
        super(client, "wss://irc-ws.chat.twitch.tv:443");
        this.config = config;

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
        listenOn(RawMessageEvent.class).doOnEach(new Consumer<Notification<RawMessageEvent>>() {
            @Override
            public void accept(Notification<RawMessageEvent> eventNotification) throws Exception {
                if (eventNotification.isOnError()) {
                    log.error(eventNotification.getError().getMessage(), eventNotification.getError());
                }
                if (eventNotification.isOnNext()) {
                    RawMessageEvent<GlitchChat> event = eventNotification.getValue();

                    log.debug("Received RawMessage({})", event.getMessage());
                }
            }
        }).subscribe(event -> getDispatcher().onNext(MessageParser.parseMessage((RawMessageEvent<GlitchChat>) event)));

        listenOn(RawIRCEvent.class).subscribe(IRConsumer::composeFrom);
    }
}
