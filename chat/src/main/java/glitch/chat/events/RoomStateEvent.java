package glitch.chat.events;

import glitch.socket.utils.EventImmutable;
import java.util.Locale;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface RoomStateEvent extends ChannelEvent {
    @Nullable
    Locale getBroadcasterLanguage();

    boolean isEmoteOnly();

    Long getFollowersOnlyTime();

    boolean hasRobot9000();

    long getSlow();

    boolean subscribersOnly();

    @Value.Lazy
    default boolean hasSlow() {
        return getSlow() > 0;
    }

    @Value.Lazy
    default boolean isFollowersOnly() {
        return getFollowersOnlyTime() != -1L;
    }
}
