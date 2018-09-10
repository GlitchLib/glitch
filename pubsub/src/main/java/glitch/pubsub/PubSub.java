package glitch.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import glitch.GlitchClient;
import glitch.core.utils.ws.WebSocketClient;
import java.net.MalformedURLException;

public class PubSub extends WebSocketClient {
    private final ObjectMapper mapper;

    public PubSub(GlitchClient client, ObjectMapper mapper) throws MalformedURLException {
        super("wss://pubsub-edge.twitch.tv/", client);
        this.mapper = mapper;
    }
}
