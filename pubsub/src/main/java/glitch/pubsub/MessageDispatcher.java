package glitch.pubsub;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import glitch.pubsub.events.MessageReceivedEvent;
import glitch.pubsub.events.topics.*;
import glitch.pubsub.events.topics.subs.*;
import glitch.pubsub.events.topics.playback.*;
//import glitch.pubsub.events.topics.moderation.*;
import io.reactivex.functions.Consumer;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageDispatcher implements Consumer<MessageReceivedEvent> {
    private final PubSubImpl pubSub;

    @Override
    public void accept(MessageReceivedEvent event) throws Exception {
        switch (event.getData().getTopic().getType()) {
            case FOLLOW:
                FollowEventImpl follow = (FollowEventImpl) pubSub.gson.fromJson(event.getData().getMessage(), FollowEvent.class);
                pubSub.getDispatcher().onNext(follow.withChannelId(Long.parseLong(event.getData().getTopic().getSuffix()[0])));
                break;
            case WHISPERS:
                WhisperMode mode = pubSub.gson.fromJson(event.getData().getMessage(), WhisperMode.class);
                switch (mode.type) {
                    case THREAD:
                        pubSub.getDispatcher().onNext(pubSub.gson.fromJson(mode.data, WhisperThreadEvent.class));
                }
                break;
            case CHANNEL_BITS:
                pubSub.getDispatcher().onNext(pubSub.gson.fromJson(event.getData().getMessage(), BitsEvent.class));
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

    @Data
    private static class WhisperMode {
        @JsonAdapter(value = WhisperTypeAdapter.class)
        private final Type type;
        private final String data;

        enum Type {
            WHISPER_RECEIVED,
            WHISPER_SENT,
            THREAD
        }


        private class WhisperTypeAdapter implements JsonSerializer<Type>, JsonDeserializer<Type> {

            @Override
            public JsonElement serialize(Type src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.name().toLowerCase());
            }

            @Override
            public Type deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                for (Type type : Type.values()) {
                    if (type.name().equalsIgnoreCase(json.getAsString())) {
                        return type;
                    }
                }
                throw new JsonParseException("Unknown whisper type: " + json.getAsString());
            }
        }
    }
}
