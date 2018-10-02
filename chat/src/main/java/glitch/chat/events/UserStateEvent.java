package glitch.chat.events;

import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface UserStateEvent extends GlobalUserStateEvent, ChannelEvent {

    @Value.Lazy
    default boolean isMod() {
        return getBadges().stream().anyMatch(badge -> badge.getName().equalsIgnoreCase("moderator"));
    }

    @Value.Lazy
    default boolean isSubscriber() {
        return getBadges().stream().anyMatch(badge -> badge.getName().equalsIgnoreCase("subscriber"));
    }
}
