package glitch.pubsub.events.topics;

import glitch.core.api.json.IDObject;
import glitch.socket.utils.EventImmutable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable(copy = true)
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface Emote extends IDObject<Long> {
    int getStart();

    int getEnd();

    @Value.Lazy
    default String getEmoteUrl(Size emoteSize) {
        return String.format("http://static-cdn.jtvnw.net/emoticons/v1/%d/%s", getId(), emoteSize.value);
    }

    enum Size {
        X1(1.0),
        X2(2.0),
        X3(3.0);

        private final double value;

        Size(double value) {
            this.value = value;
        }
    }
}
