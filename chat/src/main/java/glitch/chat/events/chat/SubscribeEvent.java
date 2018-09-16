package glitch.chat.events.chat;

import glitch.core.api.json.enums.SubscriptionType;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface SubscribeEvent extends OrdinalMessageEvent {
    int getMonths();

    SubscriptionType getSubscriptionType();
}
