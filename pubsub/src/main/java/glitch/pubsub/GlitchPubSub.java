package glitch.pubsub;

import glitch.GlitchClient;
import glitch.socket.GlitchWebSocket;
import io.reactivex.Single;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * PubSub enables you to subscribe to a topic, for updates (e.g., when a user cheers on a channel).
 * <p>
 * The Twitch PubSub system allows back-end services to broadcast realtime messages to clients. Example applications include:
 *
 * <ul>
 * <li>An instant messaging service sending instant messages between friends.</li>
 * <li>A back-end video system pushing real-time viewer count updates to video players.</li>
 * <li>A presence system broadcasting users’ online status to all their friends.</li>
 * </ul>
 *
 * @see Topic
 */
public interface GlitchPubSub extends GlitchWebSocket {

    static Builder builder(GlitchClient client) {
        return new Builder(client);
    }

    PubSubTopics getTopics();

    Single<Void> sendPing();

    Single<Void> sendPong();

    @Data
    @Getter(AccessLevel.NONE)
    @Accessors(chain = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    class Builder {
        private final GlitchClient client;

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

        public Single<GlitchPubSub> buildAsync() {
            return Single.just(new PubSubImpl(client, topic));
        }

        public GlitchPubSub build() {
            return buildAsync().blockingGet();
        }
    }
}
