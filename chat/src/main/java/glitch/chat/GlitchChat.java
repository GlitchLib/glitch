package glitch.chat;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import feign.Feign;
import glitch.GlitchClient;
import glitch.auth.Credential;
import glitch.auth.UserCredential;
import glitch.chat.api.ChatAPI;
import glitch.chat.api.json.GlobalUserState;
import glitch.core.utils.GlitchUtils;
import glitch.core.utils.http.HTTP;
import glitch.core.utils.http.instances.KrakenInstance;
import glitch.socket.GlitchWebSocket;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import java.lang.reflect.Type;
import java.util.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

public interface GlitchChat extends GlitchWebSocket {

    static Builder builder(GlitchClient client) {
        return new Builder(client);
    }

    ChatAPI getApi();

    void joinChannel(String channel);

    void partChannel(String channel);

    default void leaveChannel(String channel) {
        partChannel(channel);
    }

    void sendMessage(String channel, String message);

    void sendPrivateMessage(String username, String message);

    @Getter
    @Setter
    @Accessors(fluent = true, chain = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    class Builder {
        private final GlitchClient client;
        private final Set<String> channels = new LinkedHashSet<>();
        private UserCredential botCredential;

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

        private Single<ChatAPI> getApi() {
            return Single.fromCallable(() -> {
                Multimap<String, String> headers = LinkedHashMultimap.create();

                headers.put("Client-ID", client.getConfiguration().getClientId());
                headers.put("User-Agent", client.getConfiguration().getUserAgent());
                headers.put("Accept", "application/vnd.twitchtv.v5+json");

                Map<Type, Object> adapters = new LinkedHashMap<>();

                Feign feign = HTTP.create(headers, GlitchUtils.createGson(adapters, true));

                return feign.newInstance(new KrakenInstance<>(ChatAPI.class));
            });
        }

        private Single<BotConfig> createBotConfig(SingleSource<ChatAPI> api) {
            Objects.requireNonNull(botCredential, "Credentials for bot must be not nullable.");

            Single<Credential> credential = client.getCredentialManager().buildFromCredentials(botCredential);
            Single<GlobalUserState> gus = Single.wrap(api).zipWith(credential, ChatAPI::getGlobalUserState)
                    .flatMap(global -> global);

            return credential.zipWith(gus, BotConfig::from);
        }

        public Single<GlitchChat> buildAsync() {
            Single<ChatAPI> api = getApi();

            return createBotConfig(api).zipWith(api, (config, chatApi) -> new ChatImpl(client, config, chatApi))
                    .cast(GlitchChat.class)
                    .doOnSuccess(client -> channels.forEach(client::joinChannel));
        }

        public GlitchChat build() {
            return buildAsync().blockingGet();
        }
    }
}
