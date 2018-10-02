package glitch.chat.events;

import glitch.core.api.json.enums.SubscriptionType;
import glitch.socket.utils.EventImmutable;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface SubscribeEvent extends ChannelMessageEvent {
    int getMonths();

    @Nullable
    String getGifterUsername();

    @Value.Lazy
    default boolean isGiftedSub() {
        return getGifterUsername() != null;
    }

    SubscriptionType getSubscriptionType();
}
