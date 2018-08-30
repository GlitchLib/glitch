package glitch.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import glitch.events.EventManager;
import glitch.ws.WebSocketClient;
import java.net.MalformedURLException;

public class PubSub extends WebSocketClient {
    private final ObjectMapper mapper;

    public PubSub(EventManager eventBus, ObjectMapper mapper) throws MalformedURLException {
        super("wss://pubsub-edge.twitch.tv/", eventBus);
        this.mapper = mapper;
    }
}
