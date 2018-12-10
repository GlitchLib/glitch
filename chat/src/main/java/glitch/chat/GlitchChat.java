package glitch.chat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import glitch.GlitchClient;
import glitch.api.AbstractWebSocketService;
import glitch.api.ws.events.IEvent;
import glitch.api.ws.events.OpenEvent;
import glitch.auth.UserCredential;
import glitch.auth.objects.json.Credential;
import glitch.chat.exceptions.AlreadyJoinedChannelException;
import glitch.chat.exceptions.NotJoinedChannelException;
import glitch.chat.exceptions.WhispersExceededException;
import glitch.chat.object.entities.ChannelEntity;
import glitch.chat.object.entities.UserEntity;
import glitch.kraken.GlitchKraken;
import glitch.kraken.services.UserService;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import javax.annotation.Nullable;
import java.nio.channels.NotYetConnectedException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class GlitchChat extends AbstractWebSocketService<GlitchChat> {

    private static final String URI_SECURE = "wss://irc-ws.chat.twitch.tv:443";
    private static final String URI = "ws://irc-ws.chat.twitch.tv:80";

    @Getter
    private final Configuration configuration;

    @Getter
    @Nullable
    private final GlitchKraken api;

    @Getter
    private final Set<ChannelEntity> channels = new LinkedHashSet<>();

    private Cache<UserEntity, Object> whispers;

    protected GlitchChat(
            GlitchClient client,
            Configuration configuration,
            FluxProcessor<IEvent<GlitchChat>, IEvent<GlitchChat>> eventProcessor,
            boolean secure,
            @Nullable GlitchKraken api) {
        super(client, (secure) ? URI_SECURE : URI, eventProcessor, new IrcConverter());
        this.configuration = configuration;
        this.api = api;

        compose();
    }

    public Mono<UserEntity> getBot() {
        return getUser(configuration.getBotCredentials().getLogin());
    }

    public Mono<ChannelEntity> getChannel(String channel) { return getChannel(channel, false); }

    public Mono<ChannelEntity> joinChannel(String channel) {
        return getChannel(channel, true);
    }

    public Mono<UserEntity> getUser(String login) {
        if (isOpen()) return Mono.error(new NotYetConnectedException());
        return Mono.just(new UserEntity(this, login));
    }

    public Mono<Void> whisper(UserEntity userEntity, Publisher<String> message) {
        if (whispers.asMap().containsKey(userEntity)) {
            return Mono.justOrEmpty(whispers.asMap().keySet().stream().filter(userEntity::equals).findFirst())
                    .zipWith(configuration.getWhisperLimits().map(Tuple2::getT1))
                    .flatMap(tuple -> send(Flux.from(message).delayElements(Duration.ofMillis(tuple.getT2()))
                            .map(msg -> String.format("PRIVMSG #jtv /w %s %s", tuple.getT1().getUsername(), msg))));
        } else {
            try {
                whispers.put(userEntity, new Object());
                return whisper(userEntity, message);
            } catch (IllegalArgumentException e) {
                return Mono.error(new WhispersExceededException("You will exceeded too much user via whispering!"));
            }
        }
    }

    private Mono<Void> doJoin(String name) {
        return send(Mono.just(String.format("JOIN #%s", name.toLowerCase())));
    }

    public static Builder builder(GlitchClient client) {
        return new Builder(client);
    }

    @SuppressWarnings("unchecked")
    private void compose() {
        if (this.api != null) {
            this.api.use(UserService.class).flatMap(service -> service.getChatUserState(configuration.getBotCredentials().getUserId()))
                    .subscribe(gus -> {
                        configuration.setRateLimits(RateLimiter.fromUserState(gus));
                        doWhisperRateLimit();
                    });
        } else {
            configuration.setRateLimits(RateLimiter.global());
            doWhisperRateLimit();
        }

        listenOn(OpenEvent.class)
                .map(e -> (OpenEvent<GlitchChat>) e)
                .subscribe(event -> {
                    channels.forEach(channel -> {
                        doJoin(channel.getChannelName()).block();
                    });
                    if (channels.stream().noneMatch(e -> e.getChannelName().equals("jtv"))) {
                        joinChannel("jtv");
                    }
                });
    }

    private void doWhisperRateLimit() {
        configuration.getWhisperLimits()
                .subscribe(limit -> this.whispers = CacheBuilder.newBuilder()
                        .maximumSize(limit.getT2())
                        .expireAfterWrite(Duration.ofMillis(limit.getT1())).build());
    }

    private Mono<ChannelEntity> getChannel(String name, boolean join) {
        if (isOpen()) return Mono.error(new NotYetConnectedException());
        return Flux.fromIterable(channels)
                .filter(e -> e.getChannelName().equals(name))
                .singleOrEmpty()
                .flatMap(e -> {
                    if (join) {
                        if (e == null) {
                            return doJoin(name).thenReturn(new ChannelEntity(this, name)).doOnSuccess(channels::add);
                        } else
                            return Mono.error(new AlreadyJoinedChannelException("Your bot is already joined on this channel: " + name.toLowerCase())).thenReturn(e);
                    } else if (e != null) return Mono.just(e);
                    else return Mono.error(new NotJoinedChannelException("You are not connecting to this channel: " + name.toLowerCase()));
                });
    }

    @Data
    @Getter(AccessLevel.NONE)
    @Accessors(fluent = true, chain = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private final GlitchClient client;
        private FluxProcessor<IEvent<GlitchChat>, IEvent<GlitchChat>> eventProcessor = EmitterProcessor.create(true);
        private final AtomicBoolean secure = new AtomicBoolean(true);
        private final Set<String> channels = new LinkedHashSet<>();
        private UserCredential botCredential;
        private final AtomicBoolean forceQuery = new AtomicBoolean(false);
        @Setter(AccessLevel.NONE)
        private final AtomicBoolean shutdownHook = new AtomicBoolean(false);

        @Nullable
        private GlitchKraken krakenApi;

        public Builder botCredential(String accessToken, String refreshToken) {
            return botCredential(new UserCredential(accessToken, refreshToken));
        }

        public Builder joinChannel(String... channel) {
            return joinChannel(Arrays.asList(channel));
        }

        public Builder joinChannel(Collection<String> channels) {
            this.channels.addAll(channels);
            return this;
        }

        public Builder addShutdownHook() {
            this.shutdownHook.set(true);
            return this;
        }

        public Builder seucre(boolean secure) {
            this.secure.set(secure);
            return this;
        }

        public Builder forceQuery(boolean forceQuery) {
            this.forceQuery.set(forceQuery);
            return this;
        }

        private Mono<Credential> createBotConfig() {
            Objects.requireNonNull(botCredential, "Credentials for bot must be not nullable.");

            return client.getCredentialManager().buildFromCredentials(botCredential);
        }

        public Mono<GlitchChat> buildAsync() {
            return createBotConfig().map(credential -> new GlitchChat(client, new Configuration(credential, forceQuery.get()), eventProcessor, secure.get(), getApi()))
                    .doOnSuccess(client -> {
                        channels.forEach(client::joinChannel);
                        if (shutdownHook.get()) {
                            Runtime.getRuntime().addShutdownHook(new Thread(client::close));
                        }
                    });
        }

        @Nullable
        private GlitchKraken getApi() {
            try {
                Class.forName("glitch.kraken.GlitchKraken");
                return GlitchKraken.create(client);
            } catch (ClassNotFoundException e) {
                log.warn("Couldn't create KrakenAPI instance cause you don't have it imported into your project [artifactId: \"glitch-kraken\"]");
                log.warn("In current state will be use a default Rate Limiter.");
                return null;
            }
        }

        public GlitchChat build() {
            return buildAsync().block();
        }
    }
}
