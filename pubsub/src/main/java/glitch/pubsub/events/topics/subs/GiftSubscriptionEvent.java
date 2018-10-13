package glitch.pubsub.events.topics.subs;

import glitch.socket.utils.EventImmutable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface GiftSubscriptionEvent extends SubscriptionEvent {
    Long getRecipientId();

    String getRecipientUserName();

    String getRecipientDisplayName();
}
