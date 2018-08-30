package glitch.chat;

import glitch.events.EventManager;
import glitch.ws.WebSocketClient;
import java.net.MalformedURLException;

public class MessageInterface extends WebSocketClient {
    public MessageInterface(EventManager eventBus) throws MalformedURLException {
        super("wss://irc-ws.chat.twitch.tv/", eventBus);
    }
}
