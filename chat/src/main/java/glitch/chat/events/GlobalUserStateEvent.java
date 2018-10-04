package glitch.chat.events;

import glitch.chat.GlitchChat;
import glitch.core.api.json.Badge;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import java.awt.Color;
import java.util.Set;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface GlobalUserStateEvent extends Event<GlitchChat> {
    Set<Badge> getBadges();

    @Nullable
    Color getColor();

    String getDisplayName();

    long getUserId();

    @Value.Lazy
    default boolean isTurbo() {
        return getBadges().stream().anyMatch(badge -> badge.getName().equalsIgnoreCase("turbo"));
    }

    @Value.Lazy
    default boolean isPrime() {
        return getBadges().stream().anyMatch(badge -> badge.getName().equalsIgnoreCase("premium"));
    }

    @Value.Lazy
    default boolean isAdmin() {
        return getBadges().stream().anyMatch(badge -> badge.getName().equalsIgnoreCase("admin"));
    }

    @Value.Lazy
    default boolean isStaff() {
        return getBadges().stream().anyMatch(badge -> badge.getName().equalsIgnoreCase("staff"));
    }

    @Value.Lazy
    default boolean isGlobalMod() {
        return getBadges().stream().anyMatch(badge -> badge.getName().equalsIgnoreCase("global_mod"));
    }
}
