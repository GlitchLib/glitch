package glitch.chat;

import glitch.GlitchClient;
import glitch.auth.Credential;
import glitch.auth.CredentialCreator;
import glitch.chat.json.BotInfo;
import glitch.common.api.BaseURL;
import glitch.common.api.HttpClient;
import glitch.common.api.HttpMethod;
import glitch.common.api.Router;
import glitch.common.events.EventManager;
import glitch.common.utils.HttpUtils;
import glitch.common.ws.WebSocketClient;
import glitch.common.ws.event.message.RawMessageReceived;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import java.net.MalformedURLException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

public class MessageInterface extends WebSocketClient {
    private final ChannelCache channelCache = new ChannelCache();

    private MessageInterface(GlitchClient client, BotConfig botConfig) throws MalformedURLException {
        super("wss://irc-ws.chat.twitch.tv/", client);
        this.getClient().getEventManager().on(RawMessageReceived.class)
                .subscribe(raw -> this.getClient().getEventManager().dispatch(MessageParser.parseMessage(raw.getMessage().toString())));
    }

    public static Builder builder(GlitchClient client) {
        return new Builder(client);
    }

    @Getter
    @Setter
    @Accessors(fluent = true, chain = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private final GlitchClient client;
        private CredentialCreator botCredential;

        public Builder botCredential(String accessToken, String refreshToken) {
            return botCredential(Credential.from(accessToken, refreshToken));
        }

        public MessageInterface buildAsync() throws Exception {
            return new MessageInterface(client, createBotConfig(client));
        }

        private BotConfig createBotConfig(GlitchClient client) throws Exception {
            Credential credential = client.getCredentialManager().create(botCredential);

            BotInfo botInfo = Router.<BotInfo>create(HttpMethod.GET, BaseURL.create("https://api.twitch.tv/kraken")
                    .endpoint("/users/%s/chat"))
                    .request(credential.getUserId())
                    .exchange(HttpUtils.createForApi(client.getConfiguration(), true));

            return BotConfig.from(credential, botInfo);
        }

        public Single<MessageInterface> build() {
            return Single.fromCallable(this::buildAsync);
        }

        public MessageInterface loginAsync() throws Exception {
            MessageInterface tmi = buildAsync();
            tmi.connectAsync();

            return tmi;
        }

        public Single<MessageInterface> login() {
            return build().flatMap(tmi -> tmi.connect().flatMap(x -> Single.just(tmi)));
        }
    }
}
