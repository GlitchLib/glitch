package glitch.pubsub;

import com.google.gson.Gson;
import glitch.GlitchClient;
import glitch.socket.GlitchWebSocket;
import glitch.socket.GlitchWebSocketImpl;

public class PubSubImpl extends GlitchWebSocketImpl implements GlitchPubSub {
    private final Gson gson;

    PubSubImpl(GlitchClient client, Gson gson) {
        super(client, "wss://pubsub-edge.twitch.tv/");
        this.gson = gson;
    }

    @Override
    public void onMessage(String message) {

    }
}
