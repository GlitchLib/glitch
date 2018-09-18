package glitch.chat;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.annotations.SerializedName;
import feign.Feign;
import glitch.GlitchClient;
import glitch.auth.Credential;
import glitch.auth.UserCredential;
import glitch.core.utils.GlitchUtils;
import glitch.core.utils.http.HTTP;
import glitch.core.utils.http.ResponseException;
import glitch.core.utils.http.instances.KrakenInstance;
import glitch.socket.GlitchWebSocket;
import io.reactivex.Single;
import java.awt.Color;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;

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
            headers.put("Accept", "application/vnd.twitchtv.v5+json");

            Map<Type, Object> adapters = new LinkedHashMap<>();

            Single<ChatDetails> chatDetails = Single.create(sub -> {
                Feign feign = HTTP.create(headers, GlitchUtils.createGson(adapters, true));

                sub.onSuccess(feign.newInstance(new KrakenInstance<>(ChatDetails.class)));
            });

            Single<Credential> credential = client.getCredentialManager().buildFromCredentials(botCredential);

            return credential.zipWith(chatDetails, (cred, chat) -> BotConfig.from(cred, chat.getBotChatInfo(cred.getUserId())));
        }

        public Single<GlitchChat> buildAsync() {
            return createBotConfig().map(config -> new GlitchChatImpl(client, config, channels)).cast(GlitchChat.class);
        }

        public GlitchChat build() {
            return buildAsync().blockingGet();
        }

        private interface ChatDetails {
            @GET @Path("/users/{id}/chat")
            BotConfigInfo getBotChatInfo(@PathParam("id") Long userId) throws ResponseException;
        }
    }

    @Value
    class BotConfigInfo {
        @SerializedName("is_known_bot")
        private final boolean knownBot;
        @SerializedName("is_verified_bot")
        private final boolean verifiedBot;
        private final Color color;
    }
}
