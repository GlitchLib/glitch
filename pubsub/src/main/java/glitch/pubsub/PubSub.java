package glitch.pubsub;

import com.google.gson.Gson;
import glitch.GlitchClient;
import glitch.socket.GlitchWebSocket;

public class PubSub extends GlitchWebSocket {
    private final Gson gson;

    public PubSub(GlitchClient client, Gson gson) {
        super(client, "wss://pubsub-edge.twitch.tv/");
        this.gson = gson;
    }

    @Override
    public void onMessage(String message) {

    }
}
