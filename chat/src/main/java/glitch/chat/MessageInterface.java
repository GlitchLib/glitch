package glitch.chat;

import glitch.events.EventManager;
import glitch.ws.WebSocketClient;
import glitch.ws.event.message.RawMessageReceived;
import java.net.MalformedURLException;

public class MessageInterface extends WebSocketClient {
    private final ChannelCache channelCache = new ChannelCache();

    public MessageInterface(EventManager eventBus) throws MalformedURLException {
        super("wss://irc-ws.chat.twitch.tv/", eventBus);
        this.manager.on(RawMessageReceived.class).subscribe(raw -> this.manager.dispatch(MessageParser.parseMessage(raw.getMessage().toString())));
    }
}
