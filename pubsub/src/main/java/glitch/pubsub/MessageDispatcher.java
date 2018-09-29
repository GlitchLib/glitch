package glitch.pubsub;

import glitch.core.api.json.enums.SubscriptionType;
import glitch.pubsub.events.MessageReceivedEvent;
import glitch.pubsub.events.topics.CommerceEvent;
import glitch.pubsub.events.topics.GiftSubscriptionEvent;
import glitch.pubsub.events.topics.SubscriptionContext;
import glitch.pubsub.events.topics.SubscriptionEvent;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageDispatcher implements Consumer<MessageReceivedEvent> {
    private final PubSubImpl pubSub;

    @Override
    public void accept(MessageReceivedEvent event) throws Exception {
        switch (event.getData().getTopic().getType()) {
            case FOLLOW:
                break;
            case WHISPERS:
                break;
            case CHANNEL_BITS:
                break;
            case VIDEO_PLAYBACK:
                break;
            case CHANNEL_COMMERCE:
                pubSub.getDispatcher().onNext(pubSub.gson.fromJson(event.getData().getMessage(), CommerceEvent.class));
                break;
            case CHANNEL_SUBSCRIPTION:
                SubscriptionEvent subEvent = pubSub.gson.fromJson(event.getData().getMessage(), SubscriptionEvent.class);

                if (subEvent.getContext().equals(SubscriptionContext.SUBGIFT)) {
                    pubSub.getDispatcher().onNext(pubSub.gson.fromJson(event.getData().getMessage(), GiftSubscriptionEvent.class));
                } else {
                    pubSub.getDispatcher().onNext(subEvent);
                }
                break;
            case CHAT_MODERATION_ACTIONS:
                break;
            case CHANNEL_EXTENSION_BROADCAST:
                break;
        }
    }
}
