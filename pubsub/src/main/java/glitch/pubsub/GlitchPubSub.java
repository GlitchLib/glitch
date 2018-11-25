package glitch.pubsub;

import com.google.gson.*;
import glitch.GlitchClient;
import glitch.api.AbstractWebSocketService;
import glitch.api.objects.adapters.ColorAdapter;
import glitch.api.objects.adapters.SubscriptionTypeAdapter;
import glitch.api.objects.adapters.UserTypeAdapter;
import glitch.api.objects.enums.SubscriptionType;
import glitch.api.objects.enums.UserType;
import glitch.api.ws.events.IEvent;
import glitch.auth.Scope;
import glitch.auth.objects.adapters.ScopeAdapter;
import glitch.pubsub.object.adapters.TopicHandler;
import glitch.pubsub.object.enums.MessageType;
import glitch.pubsub.object.json.SingleRequestType;
import glitch.pubsub.object.json.TopicRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public final class GlitchPubSub extends AbstractWebSocketService<GlitchPubSub> {

    private static final String URI_SECURE = "wss://pubsub-edge.twitch.tv";
    private static final String URI = "ws://pubsub-edge.twitch.tv";

    protected final Gson gson = gsonInit();
    @Getter
    private final TopicsCache topics = new TopicsCache(this);

    private GlitchPubSub(
            GlitchClient client,
            FluxProcessor<IEvent<GlitchPubSub>, IEvent<GlitchPubSub>> eventProcessor,
            boolean secure
    ) {
        super(client, (secure) ? URI_SECURE : URI, eventProcessor, new PubSubConverter());
    }


    Mono<Void> buildMessage(MessageType type, @Nullable Topic topic) {
        if (Arrays.asList(MessageType.LISTEN, MessageType.UNLISTEN).contains(type) && topic != null) {
            JsonArray topics = new JsonArray();
            topics.add(new JsonPrimitive(topic.getRawType()));

            JsonObject dataType = new JsonObject();
            dataType.add("topics", topics);
            if (topic.getCredential() != null) {
                dataType.add("auth_token", new JsonPrimitive(topic.getCredential().getAccessToken()));
            }

            return send(Mono.just(gson.toJson(new TopicRequest(type, topic.getCode().toString(), dataType))));
        } else {
            return send(Mono.just(gson.toJson(new SingleRequestType(type))));
        }
    }

    private Gson gsonInit() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Scope.class, new ScopeAdapter());
        gsonBuilder.registerTypeAdapter(Color.class, new ColorAdapter());
        gsonBuilder.registerTypeAdapter(UserType.class, new UserTypeAdapter());
        gsonBuilder.registerTypeAdapter(SubscriptionType.class, new SubscriptionTypeAdapter());

        gsonBuilder.registerTypeAdapter(Topic.class, new TopicHandler(this));

        return gsonBuilder.create();
    }

    public static Builder builder(GlitchClient client) {
        return new Builder(client);
    }

    @Data
    @Accessors(chain = true, fluent = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private final GlitchClient client;
        private FluxProcessor<IEvent<GlitchPubSub>, IEvent<GlitchPubSub>> eventProcessor = EmitterProcessor.create(true);
        private final AtomicBoolean secure = new AtomicBoolean(true);

        private final Map<Topic, Boolean> topic = new LinkedHashMap<>(50);

        public Builder setTopic(Topic... topic) {
            return setTopic(Arrays.asList(topic), false);
        }

        public Builder activateTopic(Topic... topic) {
            return setTopic(Arrays.asList(topic), true);
        }

        public Builder setTopic(Collection<Topic> topic, boolean active) {
            topic.forEach(t -> this.topic.put(t, active));
            return this;
        }

        public GlitchPubSub build() {
            return new GlitchPubSub(client, eventProcessor, secure.get());
        }

        public Mono<GlitchPubSub> buildAsync() {
            return Mono.just(build());
        }
    }
}
