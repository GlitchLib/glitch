package glitch.chat;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import glitch.GlitchClient;
import glitch.auth.Credential;
import glitch.auth.UserCredential;
import glitch.core.utils.GlitchUtils;
import glitch.socket.GlitchWebSocket;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GlitchChat extends GlitchWebSocket {

    void joinChannel(String channel);

    void partChannel(String channel);

    default void leaveChannel(String channel) {
        partChannel(channel);
    }

    void sendMessage(String channel, String message);

    void sendPrivateMessage(String username, String message);

    static Builder builder(GlitchClient client) {
        return new Builder(client);
    }

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

        private <X> Single<BotConfig> createBotConfig() {
            Objects.requireNonNull(botCredential, "Credentials for bot must be not nullable.");

            Multimap<String, String> headers = LinkedHashMultimap.create();

            headers.put("Client-ID", client.getConfiguration().getClientId());
            headers.put("User-Agent", client.getConfiguration().getUserAgent());

            Map<Class<X>, JsonSerializer<X>> serializers = new LinkedHashMap<>();
            Map<Class<X>, JsonDeserializer<X>> deserializers = new LinkedHashMap<>();

            GlitchUtils.registerSerializers(serializers);
            GlitchUtils.registerDeserializers(deserializers);

            Single<ChatDetails> chatDetails = Single.create(sub -> {
                Retrofit retrofit = GlitchUtils.createHttpClient(GlitchUtils.KRAKEN, headers, serializers, deserializers);

                sub.onSuccess(retrofit.create(ChatDetails.class));
            });

            Single<Credential> credential = client.getCredentialManager().buildFromCredentials(botCredential);

            Single<BotConfig> botInfo = credential
                    .zipWith(chatDetails, (cred, chat) -> chat.getBotChatInfo(cred.getUserId()))
                    .flatMap(info -> info);
            return botInfo.zipWith(credential, (b, c) -> BotConfig.from(c, b));
        }

        public Single<GlitchChat> buildAsync() {
            return createBotConfig().map(config -> new GlitchChatImpl(client, config, channels));
        }

        public GlitchChat build() {
            return buildAsync().blockingGet();
        }

        private interface ChatDetails {
            @GET("/users/{id}/chat")
            Single<BotConfig> getBotChatInfo(@Path("id") Long userId);
        }
    }
}
