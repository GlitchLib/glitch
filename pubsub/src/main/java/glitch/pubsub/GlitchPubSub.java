package glitch.pubsub;

import com.google.gson.*;
import glitch.GlitchClient;
import glitch.api.ws.WebSocket;
import glitch.api.ws.events.IEvent;
import glitch.api.ws.events.PingEvent;
import glitch.pubsub.events.PubSubEvent;
import glitch.pubsub.object.enums.MessageType;
import glitch.pubsub.events.v1.json.SingleRequestType;
import glitch.pubsub.events.v1.json.TopicRequest;
import glitch.service.ISocketService;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.Mono;

public final class GlitchPubSub implements ISocketService<GlitchPubSub> {

    private static final String URI_SECURE = "wss://pubsub-edge.twitch.tv";
    private static final String URI = "ws://pubsub-edge.twitch.tv";

    private static final Logger LOG = LoggerFactory.getLogger(GlitchPubSub.class);
    protected final WebSocket<GlitchPubSub> ws;
    private final GlitchClient client;
    private final TopicsCache topicsCache = new TopicsCache(this);
    private final Gson gson;

    @SuppressWarnings("unchecked")
    private GlitchPubSub(
            GlitchClient client, GsonBuilder gson, boolean secure, Map<Topic, Boolean> topics,
            FluxProcessor<IEvent<GlitchPubSub>, IEvent<GlitchPubSub>> eventProcessor,
            boolean disableAutoPing,
    ) {
        this.client = client;
        this.gson = gson.registerTypeAdapter(GlitchPubSub.class, (JsonDeserializer<GlitchPubSub>) (json, type, context) -> GlitchPubSub.this).create();
        this.ws = WebSocket.builder(this)
                .addInterceptor(new HttpLoggingInterceptor(LOG::debug).setLevel(HttpLoggingInterceptor.Level.BASIC))
                .setEventConverter(new PubSubConverter(this.gson))
                .setEventProcessor(eventProcessor)
                .build((secure) ? URI_SECURE : URI);
        topics.forEach(this.topicsCache::register);
        this.ws.onEvent(PubSubEvent.class)
                .subscribe(PubSubUtils::consume);
        this.ws.onEvent(PingEvent.class)
                .subscribe(ping -> {
                    if (!disableAutoPing) {
                        this.ws.send(Mono.just("{\"type\": \"PONG\"}"));
                    }
                });
    }

    public static Builder builder(GlitchClient client) {
        return new Builder(client);
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

            return this.ws.send(Mono.just(gson.toJson(new TopicRequest(type, topic.getCode().toString(), dataType))));
        } else {
            return this.ws.send(Mono.just(gson.toJson(new SingleRequestType(type))));
        }
    }

    public TopicsCache getTopicsCache() {
        return this.topicsCache;
    }

    @Override
    public Mono<Void> login() {

        return this.ws.connect()
                .then(doInit());
    }

    @Override
    public void logout() {
        this.ws.disconnect();
    }

    @Override
    public Mono<Void> retry() {
        return this.ws.retry().then(doInit());
    }

    @Override
    public <E extends IEvent<GlitchPubSub>> Flux<E> onEvent(Class<E> type) {
        return this.ws.onEvent(type);
    }

    @Override
    public GlitchClient getClient() {
        return client;
    }

    private Mono<Void> doInit() {
        return Flux.fromIterable(topicsCache.getActive())
                .flatMap(t -> buildMessage(MessageType.LISTEN, t))
                .then();
    }

    public static class Builder {
        private final GlitchClient client;
        private final GsonBuilder gsonBuilder = new GsonBuilder();
        private final AtomicBoolean secure = new AtomicBoolean(true);
        private final Map<Topic, Boolean> topics = new LinkedHashMap<>(50);
        private final AtomicBoolean disableAutoPing = new AtomicBoolean(false);
        private FluxProcessor<IEvent<GlitchPubSub>, IEvent<GlitchPubSub>> eventProcessor = EmitterProcessor.create(true);

        private Builder(GlitchClient client) {
            this.client = client;
        }

        public Builder setTopics(Topic... topics) {
            return setTopics(Arrays.asList(topics), false);
        }

        public Builder activateTopic(Topic... topic) {
            return setTopics(Arrays.asList(topic), true);
        }

        public Builder setTopics(Collection<Topic> topic, boolean active) {
            topic.forEach(t -> this.topics.put(t, active));
            return this;
        }

        public Mono<GlitchPubSub> buildAsync() {
            return Mono.just(build());
        }

        public Builder setEventProcessor(FluxProcessor<IEvent<GlitchPubSub>, IEvent<GlitchPubSub>> eventProcessor) {
            this.eventProcessor = eventProcessor;
            return this;
        }

        public Builder setDisableAutoPing(boolean disableAutoPing) {
            this.disableAutoPing.set(disableAutoPing);
            return this;
        }

        public GlitchPubSub build() {
            registerDefaultAdapters();

            return new GlitchPubSub(client, gsonBuilder, secure.get(), topics, eventProcessor, disableAutoPing.get());
        }

        private void registerDefaultAdapters() {
        }
    }
}
