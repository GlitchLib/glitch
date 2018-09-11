package glitch.chat;

import glitch.GlitchClient;
import glitch.auth.Credential;
import glitch.auth.CredentialCreator;
import glitch.chat.json.BotInfo;
import glitch.core.utils.api.BaseURL;
import glitch.core.utils.api.HttpMethod;
import glitch.core.utils.api.Router;
import glitch.core.utils.HttpUtils;
import glitch.core.utils.ws.WebSocketClient;
import glitch.core.utils.ws.event.message.RawMessageReceived;
import io.reactivex.Single;
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

        private Single<BotConfig> createBotConfig() {
            Single<Credential> credential = client.getCredentialManager().create(botCredential);

            Single<BotInfo> botInfo = credential
                    .flatMap(c -> Router.create(HttpMethod.GET, BaseURL.create("https://api.twitch.tv/kraken").endpoint("/users/%s/chat"), BotInfo.class)
                    .request(c.getUserId())
                    .exchange(HttpUtils.createForApi(client.getConfiguration(), true)));

            return botInfo.zipWith(credential, (b, c) -> BotConfig.from(c, b));
        }

        public Single<MessageInterface> build() {
            return createBotConfig().map(config -> new MessageInterface(client, config));
        }

        public Single<MessageInterface> login() {
            return build().flatMap(tmi -> tmi.connect().flatMap(x -> Single.just(tmi)));
        }
    }
}
