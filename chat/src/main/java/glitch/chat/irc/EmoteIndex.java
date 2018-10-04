package glitch.chat.irc;

import com.google.common.collect.ImmutableRangeSet;
import glitch.core.api.json.IDObject;
import glitch.core.utils.Immutable;
import org.immutables.value.Value;

/**
 * Information to replace text in the message with emote images.
 * Emotes can be replaced into emote from strings using {@link java.lang.String#indexOf(int) {@code indexOf()}}
 *
 * @author Damian Staszewski
 * @see com.google.common.collect.Range
 * @see com.google.common.collect.ImmutableSet
 * @see glitch.core.api.json.IDObject
 * @since 0.2.0
 */
@Immutable
@Value.Immutable(builder = false)
public interface EmoteIndex extends IDObject<Integer> {
    ImmutableRangeSet<Integer> getIndexRange();

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
