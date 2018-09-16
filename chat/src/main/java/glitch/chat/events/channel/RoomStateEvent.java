package glitch.chat.events.channel;

import glitch.socket.utils.EventImmutable;
import java.util.Locale;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface RoomStateEvent extends ChannelEvent {
    @Nullable
    Locale getBroadcasterLanguage();

    boolean hasRobot9000();

    long getSlow();

    boolean subscribersOnly();

    @Value.Lazy
    default boolean hasSlow() {
        return getSlow() > 0;
    }
}
