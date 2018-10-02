package glitch.pubsub;

import com.google.gson.Gson;
import glitch.GlitchClient;
import glitch.core.utils.GlitchUtils;
import glitch.pubsub.adapters.PubSubAdapter;
import glitch.pubsub.adapters.TopicAdapter;
import glitch.pubsub.events.Message;
import glitch.pubsub.events.MessageReceivedEvent;
import glitch.pubsub.events.ReconnectEventImpl;
import glitch.pubsub.events.Response;
import glitch.pubsub.events.TopicRegisteredEventImpl;
import glitch.pubsub.events.TopicUnregisteredEventImpl;
import glitch.pubsub.topics.Topic;
import glitch.socket.WebSocketImpl;
import glitch.socket.events.actions.OpenEvent;
import glitch.socket.events.actions.PingEventImpl;
import glitch.socket.events.actions.PongEventImpl;
import glitch.socket.events.message.RawMessageEvent;
import io.reactivex.Single;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.Getter;

public class PubSubImpl extends WebSocketImpl implements GlitchPubSub {
    final Gson gson;
    @Getter
    private final PubSubTopics topics;

    PubSubImpl(GlitchClient client, Map<Topic, Boolean> topics) {
        super(client, "wss://pubsub-edge.twitch.tv/");
        this.gson = GlitchUtils.createGson(getAdapters(), true);
        this.topics = new PubSubTopics(this);
        topics.forEach(this.topics::register);
    }

    public Single<Void> sendPing() {
        return sendRequest(Message.Type.PING, null);
    }

    public Single<Void> sendPong() {
        return sendRequest(Message.Type.PONG, null);
    }

    Single<Void> sendRequest(Message.Type type, @Nullable Topic topic) {
        Map<String, Object> request = new LinkedHashMap<>();

        request.put("type", type.name());
        if ((type == Message.Type.LISTEN || type == Message.Type.UNLISTEN) && topic != null) {
            Map<String, Object> topicData = new LinkedHashMap<>();

            topicData.put("topics", Collections.singleton(topic.getRawType()));

            if (topic.getCredential() != null) {
                topicData.put("auth_token", topic.getCredential().getAccessToken());
            }

            request.put("nonce", topic.getCode().toString());
            request.put("data", topicData);
        }

        return Single.create(ignore -> ws.send(gson.toJson(request)));
    }

    private void registerListenrers() {
        listenOn(OpenEvent.class).subscribe(event -> this.topics.getActive().forEach(topic -> this.sendRequest(Message.Type.LISTEN, topic)));
        listenOn(RawMessageEvent.class).subscribe(event -> {
            Message msg = gson.fromJson(event.getMessage(), Message.class);
            switch (msg.getType()) {
                case PING:
                    dispatcher.onNext(PingEventImpl.of(Instant.now(), this));
                    break;
                case PONG:
                    dispatcher.onNext(PongEventImpl.of(Instant.now(), this));
                    break;
                case RECONNECT:
                    dispatcher.onNext(ReconnectEventImpl.of(Instant.now(), this));
                    reconnect();
                    break;
                case RESPONSE:
                    Response response = gson.fromJson(event.getMessage(), Response.class);
                    this.topics.getTopicSet().stream()
                            .filter(entry -> entry.getKey().getCode().toString().equals(response.getNonce()))
                            .forEach(entry -> {
                                if (entry.getValue()) {
                                    dispatcher.onNext(TopicRegisteredEventImpl.of(entry.getKey(), Instant.now(), this));
                                } else {
                                    dispatcher.onNext(TopicUnregisteredEventImpl.of(entry.getKey(), Instant.now(), this));
                                }
                            });
                    break;
                case MESSAGE:
                    dispatcher.onNext(gson.fromJson(event.getMessage(), MessageReceivedEvent.class));
                    break;
            }
        });
        listenOn(MessageReceivedEvent.class).subscribe(new MessageDispatcher(this));
    }

    private Map<Type, Object> getAdapters() {
        Map<Type, Object> adapters = new LinkedHashMap<>();

        adapters.put(Topic.class, new TopicAdapter(this));
        adapters.put(GlitchPubSub.class, new PubSubAdapter(this));

        return adapters;
    }
}
