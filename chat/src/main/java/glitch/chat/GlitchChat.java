package glitch.chat;

import glitch.GlitchClient;
import glitch.api.ws.WebSocket;
import glitch.api.ws.events.IEvent;
import glitch.api.ws.events.PingEvent;
import glitch.auth.UserCredential;
import glitch.auth.objects.json.Credential;
import glitch.chat.events.IRCEvent;
import glitch.chat.exceptions.AlreadyJoinedChannelException;
import glitch.chat.exceptions.NotJoinedChannelException;
import glitch.service.ISocketService;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.logging.HttpLoggingInterceptor;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class GlitchChat implements ISocketService<GlitchChat> {
    private static final Logger LOG = LoggerFactory.getLogger(GlitchChat.class);

    private static final String URI_SECURE = "wss://irc-ws.chat.twitch.tv:443";
    private static final String URI = "ws://irc-ws.chat.twitch.tv:80";
    protected final WebSocket<GlitchChat> ws;
    private final Configuration configuration;
    private final Set<String> channels = new LinkedHashSet<>();
    private final GlitchClient client;

    /**
     * @param client         the {@link glitch.GlitchClient}
     * @param eventProcessor
     * @param scheduler
     */
    @SuppressWarnings("unchecked")
    private GlitchChat(
            GlitchClient client,
            FluxProcessor<IEvent<GlitchChat>, IEvent<GlitchChat>> eventProcessor,
            Scheduler scheduler,
            Configuration configuration,
            Set<String> channels,
            boolean secure) {
        this.client = client;
        this.ws = WebSocket.builder(this)
                .addInterceptor(new HttpLoggingInterceptor(LOG::debug).setLevel(HttpLoggingInterceptor.Level.BASIC))
                .setEventConverter(TmiConverter.create())
                .setEventProcessor(eventProcessor)
                .setEventScheduler(scheduler)
                .build((secure) ? URI_SECURE : URI);
        this.configuration = configuration;
        this.channels.addAll(channels);

        this.ws.onEvent(IRCEvent.class)
                .subscribe(ChatUtils::consume);
        this.ws.onEvent(PingEvent.class)
                .subscribe(ping -> {
                    if (!configuration.isDisableAutoPing()) {
                        this.ws.send(Mono.just("PONG :tmi.twitch.tv"));
                    }
                });
    }

    public static Builder builder(GlitchClient client) {
        return new Builder(client);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public GlitchClient getClient() {
        return client;
    }

    @Override
    public Mono<Void> login() {
        // TODO: Whispers
//        if (!channels.contains("jtv")) {
//            channels.add("jtv");
//        }

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

    private Mono<Void> doInit() {
        return this.ws.send(Flux.create(sink -> {
            sink.next("CAP REQ :twitch.tv/membership")
                    .next("CAP REQ :twitch.tv/tags")
                    .next("CAP REQ :twitch.tv/commands")
                    .next("PASS oauth:" + configuration.getBotCredentials().getAccessToken())
                    .next("NICK " + configuration.getBotCredentials().getLogin());

            if (!channels.isEmpty()) {
                channels.forEach(c -> sink.next("JOIN #" + c));
            }
            sink.complete();
        }));
    }

    @Override
    public <E extends IEvent<GlitchChat>> Flux<E> onEvent(Class<E> type) {
        return this.ws.onEvent(type);
    }

    public Mono<Void> sendChannel(String channel, Publisher<String> message) throws NotJoinedChannelException {
        final String fchannel = (channel.startsWith("#")) ?
                channel.substring(1) : channel;

        if (channels.contains(fchannel)) {
            return this.ws.send(Flux.from(message).map(m -> String.format("PRIVMSG #%s %s", channel, (m.startsWith("/")) ? m : ":" + m)));
        } else {
            return Mono.error(new NotJoinedChannelException(fchannel));
        }
    }

    public void joinChannel(String channel) throws AlreadyJoinedChannelException {
        final String fchannel = (channel.startsWith("#")) ?
                channel.substring(1) : channel;

        if (ws.isConnected()) {
            if (channels.contains(fchannel)) {
                throw new AlreadyJoinedChannelException(fchannel);
            } else {
                this.ws.send(Mono.just("JOIN #" + fchannel))
                        .doOnSuccess(v -> channels.add(fchannel))
                        .subscribe();
            }
        } else {
            channels.add(fchannel);
        }
    }

    public void leaveChannel(String channel) throws NotJoinedChannelException {
        final String fchannel = (channel.startsWith("#")) ?
                channel.substring(1) : channel;

        if (ws.isConnected()) {
            if (!channels.contains(fchannel)) {
                throw new NotJoinedChannelException(fchannel);
            } else {
                this.ws.send(Mono.just("PART #" + fchannel))
                        .doOnSuccess(v -> channels.remove(fchannel))
                        .subscribe();
            }
        } else {
            channels.remove(fchannel);
        }
    }

    public static class Builder {
        private final GlitchClient client;
        private final Set<String> channels = new LinkedHashSet<>();
        private final AtomicBoolean secure = new AtomicBoolean(true);
        private final AtomicBoolean forceQuery = new AtomicBoolean(false);
        private final AtomicBoolean shutdownHook = new AtomicBoolean(false);
        private final AtomicBoolean autoPingDisabled = new AtomicBoolean(false);
        private UserCredential botCredential;
        private Scheduler scheduler = Schedulers.fromExecutor(Executors.newWorkStealingPool(), true);
        private FluxProcessor<IEvent<GlitchChat>, IEvent<GlitchChat>> eventProcessor = EmitterProcessor.create(true);

        private Builder(GlitchClient client) {
            this.client = client;
        }

        public Builder addChannel(String... channel) {
            return addChannel(Arrays.asList(channel));
        }

        public Builder addChannel(Collection<String> channels) {
            this.channels.addAll(channels);
            return this;
        }

        public Builder addShutdownHook() {
            this.shutdownHook.set(true);
            return this;
        }

        public Builder setSecure(boolean secure) {
            this.secure.set(secure);
            return this;
        }

        public Builder setForceQuery(boolean forceQuery) {
            this.forceQuery.set(forceQuery);
            return this;
        }

        public Builder setEventProcessor(FluxProcessor<IEvent<GlitchChat>, IEvent<GlitchChat>> eventProcessor) {
            this.eventProcessor = eventProcessor;
            return this;
        }

        public Builder setBotCredential(UserCredential botCredential) {
            this.botCredential = botCredential;
            return this;
        }

        public Builder setBotCredential(String accessToken, String refreshToken) {
            return setBotCredential(new UserCredential(accessToken, refreshToken));
        }

        public Builder setScheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
            return this;
        }

        public Builder setAutoPingDisabled(boolean autoPingDisabled) {
            this.autoPingDisabled.set(autoPingDisabled);
            return this;
        }

        public Mono<GlitchChat> buildAsync() {
            return createBotConfig()
                    .map(t -> new GlitchChat(client, eventProcessor, scheduler, new Configuration(t, autoPingDisabled.get()), channels, secure.get()))
                    .doOnSuccess(client -> {
                        if (shutdownHook.get()) {
                            Runtime.getRuntime().addShutdownHook(new Thread(client::logout));
                        }
                    });
        }

        public GlitchChat build() {
            return new CompletableFuture<GlitchChat>() {
                {
                    buildAsync().subscribe(this::complete);
                }
            }.join();
        }

        private Mono<Credential> createBotConfig() {
            return client.getCredentialManager().buildFromCredentials(Objects.requireNonNull(botCredential, "Credentials for bot must be not nullable."));
        }
    }
}
