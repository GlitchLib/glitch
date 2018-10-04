package glitch.chat.events;

import glitch.core.api.json.enums.SubscriptionType;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface SubscribeGiftEvent extends ChannelUserEvent {
    SubscriptionType getSubscriptionType();

    int getGiftedCount();

    int getTotalGiftedCount();
}
