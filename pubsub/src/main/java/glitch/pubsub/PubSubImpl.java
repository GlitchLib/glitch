package glitch.pubsub;

import com.google.gson.Gson;
import glitch.GlitchClient;
import glitch.core.utils.GlitchUtils;
import glitch.pubsub.adapters.PubSubAdapter;
import glitch.pubsub.adapters.TopicAdapter;
import glitch.pubsub.events.Message;
import glitch.pubsub.events.MessageReceivedEvent;
import glitch.pubsub.events.MessageReceivedEventImpl;
import glitch.pubsub.events.ReconnectEventImpl;
import glitch.pubsub.events.Response;
import glitch.pubsub.events.TopicRegisteredEventImpl;
import glitch.pubsub.events.TopicUnregisteredEventImpl;
import glitch.pubsub.topics.Topic;
import glitch.socket.GlitchWebSocketImpl;
import glitch.socket.events.actions.OpenEvent;
import glitch.socket.events.actions.PingEventImpl;
import glitch.socket.events.actions.PongEventImpl;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;

public class PubSubImpl extends GlitchWebSocketImpl implements GlitchPubSub {
    final Gson gson;
    @Getter
    private final PubSubTopics topics;

    PubSubImpl(GlitchClient client, Map<Topic, Boolean> topics) {
        super(client, "wss://pubsub-edge.twitch.tv/");
        this.gson = GlitchUtils.createGson(getAdapters(), true);
        this.topics = new PubSubTopics(this);
        this.ping.set(gson.toJson(PingEventImpl.of(Instant.now(), UUID.randomUUID().toString(), this)));
        this.pong.set(gson.toJson(PongEventImpl.of(Instant.now(), UUID.randomUUID().toString(), this)));
        topics.forEach((topic, active) -> {
            if (active) {
                this.topics.enableTopic(topic);
            } else {
                this.topics.registerTopic(topic);
            }
        });
        listenOn(OpenEvent.class).subscribe(event -> this.topics.getActive().forEach(topic -> this.topics.sendRequest(Message.Type.LISTEN, topic)));
        listenOn(MessageReceivedEvent.class).subscribe(new MessageDispatcher(this));
    }

    @Override
    public void onMessage(String message) {
        Message msg = gson.fromJson(message, Message.class);
        switch (msg.getType()) {
            case PING:
                pong();
                dispatcher.onNext(PingEventImpl.of(Instant.now(), UUID.randomUUID().toString(), this));
                break;
            case PONG:
                dispatcher.onNext(PongEventImpl.of(Instant.now(), UUID.randomUUID().toString(), this));
                break;
            case RECONNECT:
                dispatcher.onNext(ReconnectEventImpl.of(Instant.now(), UUID.randomUUID().toString(), this));
                reconnect();
                break;
            case RESPONSE:
                Response response = gson.fromJson(message, Response.class);
                this.topics.getTopicSet().stream()
                        .filter(entry -> entry.getKey().getCode().toString().equals(response.getNonce()))
                        .forEach(entry -> {
                            if (entry.getValue()) {
                                dispatcher.onNext(TopicRegisteredEventImpl.of(entry.getKey(), Instant.now(), UUID.randomUUID().toString(), this));
                            } else {
                                dispatcher.onNext(TopicUnregisteredEventImpl.of(entry.getKey(), Instant.now(), UUID.randomUUID().toString(), this));
                            }
                        });
                break;
            case MESSAGE:
                dispatcher.onNext(gson.fromJson(message, MessageReceivedEvent.class));
                break;
        }
    }

    private Map<Type, Object> getAdapters() {
        Map<Type, Object> adapters = new LinkedHashMap<>();

        adapters.put(Topic.class, new TopicAdapter(this));
        adapters.put(GlitchPubSub.class, new PubSubAdapter(this));

        return adapters;
    }
}
