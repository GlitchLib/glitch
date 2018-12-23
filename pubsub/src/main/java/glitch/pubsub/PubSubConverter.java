package glitch.pubsub;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.api.ws.Converter;
import glitch.api.ws.events.IEvent;
import glitch.api.ws.events.PingEvent;
import glitch.api.ws.events.PongEvent;
import glitch.pubsub.events.*;
import glitch.pubsub.object.enums.SubscriptionContext;
import glitch.pubsub.object.json.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import okio.ByteString;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class PubSubConverter implements Converter<GlitchPubSub> {
    @Override
    public IEvent<GlitchPubSub> convert(ByteString value, GlitchPubSub client) {
        JsonElement r = client.gson.toJsonTree(value.utf8());
        Response response = client.gson.fromJson(r, Response.class);

        switch (response.getType()) {
            case PING:
                return new PingEvent<>(client);
            case PONG:
                return new PongEvent<>(client);
            case RECONNECT:
                return new ReconnectAlertEvent(client);
            case RESPONSE:
                return new ResponseListenedEvent(client, UUID.fromString(response.getNonce()), response.getError());
            case MESSAGE:
                return doResponseMessage(client, response.getData());
        }
        return new UnknownResponseEvent(client, r);
    }

    private IEvent<GlitchPubSub> doResponseMessage(GlitchPubSub client, ResponseData data) {
        Topic topic = data.getTopic();
        JsonObject content = data.getMessage();

        switch (topic.getType()) {
            case FOLLOW:
                return new FollowEvent(client, topic, client.gson.fromJson(content, Following.class));
            case WHISPERS:
                WhisperMode whisperMode = client.gson.fromJson(content, WhisperMode.class);
                switch (whisperMode.type) {
                    case THREAD:
                        return new WhisperThreadEvent(client, topic, client.gson.fromJson(whisperMode.data, WhisperThread.class));
                    case WHISPER_RECEIVED:
                        return new WhisperReceivedEvent(client, topic, client.gson.fromJson(whisperMode.data, WhisperMessage.class));
                    case WHISPER_SENT:
                        return new WhisperSentEvent(client, topic, client.gson.fromJson(whisperMode.data, WhisperMessage.class));
                }
                return new RawMessageEvent(client, topic, content);
            case CHANNEL_BITS:
                return new BitsEvent(client, topic, client.gson.fromJson(content, BitsMessage.class));
            case VIDEO_PLAYBACK:
                VideoPlayback playback = client.gson.fromJson(content, VideoPlayback.class);

                switch (playback.getType()) {
                    case STREAM_UP:
                        return new StreamUpEvent(client, topic, client.gson.fromJson(content, StreamUp.class));
                    case STREAM_DOWN:
                        return new StreamDownEvent(client, topic, client.gson.fromJson(content.get("server_time"), Instant.class));
                    case VIEW_COUNT:
                        return new ViewCountEvent(client, topic, client.gson.fromJson(content, ViewCount.class));
                }

                return new RawMessageEvent(client, topic, content);
            case CHANNEL_COMMERCE:
                return new CommerceEvent(client, topic, client.gson.fromJson(content, CommerceMessage.class));
            case CHANNEL_SUBSCRIPTION:
                SubscriptionMessage sub = client.gson.fromJson(content, SubscriptionMessage.class);

                if (sub.getContext().equals(SubscriptionContext.SUBGIFT)) {
                    return new SubGiftEvent(client, topic, client.gson.fromJson(content, GiftSubscriptionMessage.class));
                } else {
                    return new SubscriptionEvent(client, topic, sub);
                }
            case CHAT_MODERATION_ACTIONS:
                return doResponseModeration(client, topic, content.getAsJsonObject("data"));

            case CHANNEL_EXTENSION_BROADCAST:
                return new ChannelExtensionBroadcastEvent(client, topic, content.getAsJsonArray("content"));
        }
        return new RawMessageEvent(client, topic, content);
    }

    private IEvent<GlitchPubSub> doResponseModeration(GlitchPubSub client, Topic topic, JsonObject data) {
        ModerationData modData = client.gson.fromJson(data, ModerationData.class);
        switch (modData.getModerationAction()) {
            case TIMEOUT:
                return new TimeoutUserEvent(client, topic,
                        new Timeout(modData.getCreatedBy(),
                                modData.getCreatedById(),
                                modData.getArgs().get(0),
                                modData.getTargetId(),
                                Integer.parseInt(modData.getArgs().get(1)),
                                (modData.getArgs().size() > 2) ? modData.getArgs().get(2) : null));
            case BAN:
                return new BanUserEvent(client, topic,
                        new Ban(modData.getCreatedBy(),
                                modData.getCreatedById(),
                                modData.getArgs().get(0),
                                modData.getTargetId(),
                                (modData.getArgs().size() > 1) ? modData.getArgs().get(1) : null));
            case UNBAN:
            case UNTIMEOUT:
                return new UnbanUserEvent(client, topic, new Unban(
                        modData.getCreatedBy(),
                        modData.getCreatedById(),
                        modData.getArgs().get(0),
                        modData.getTargetId()
                ));
            case HOST:
                return new HostEvent(client, topic, new Host(
                        modData.getCreatedBy(),
                        modData.getCreatedById(),
                        modData.getArgs().get(0),
                        modData.getTargetId()
                ));
            case SUBSCRIBERS:
                return new SubscribersOnlyEvent(client, topic, new ModeratorActivation(
                        modData.getCreatedBy(),
                        modData.getCreatedById(),
                        true
                ));
            case SUBSCRIBERSOFF:
                return new SubscribersOnlyEvent(client, topic, new ModeratorActivation(
                        modData.getCreatedBy(),
                        modData.getCreatedById(),
                        false
                ));
            case CLEAR:
                return new ClearChatEvent(client, topic, new Moderator(
                        modData.getCreatedBy(),
                        modData.getCreatedById()
                ));
            case EMOTEONLY:
                return new EmoteOnlyEvent(client, topic, new ModeratorActivation(
                        modData.getCreatedBy(),
                        modData.getCreatedById(),
                        true
                ));
            case EMOTEONLYOFF:
                return new EmoteOnlyEvent(client, topic, new ModeratorActivation(
                        modData.getCreatedBy(),
                        modData.getCreatedById(),
                        false
                ));
            case R9KBETA:
                return new Robot9000Event(client, topic, new ModeratorActivation(
                        modData.getCreatedBy(),
                        modData.getCreatedById(),
                        true
                ));
            case R9KBETAOFF:
                return new Robot9000Event(client, topic, new ModeratorActivation(
                        modData.getCreatedBy(),
                        modData.getCreatedById(),
                        false
                ));
            case DELETE:
                return new MessageDeleteEvent(client, topic, new MessageDelete(
                        modData.getArgs().get(2),
                        modData.getCreatedBy(),
                        modData.getCreatedById(),
                        modData.getArgs().get(1),
                        modData.getArgs().get(0),
                        modData.getTargetId()
                ));
        }
        return new RawMessageEvent(client, topic, data);
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

        private class WhisperTypeAdapter extends TypeAdapter<Type> {
            @Override
            public void write(JsonWriter out, Type value) throws IOException {
                out.value(value.name().toLowerCase());
            }

            @Override
            public Type read(JsonReader in) throws IOException {
                for (Type t : Type.values()) {
                    if (t.name().equalsIgnoreCase(in.nextString())) {
                        return t;
                    }
                }
                throw new JsonParseException("Unknown whisper type: " + in.nextString());
            }
        }
    }
}
