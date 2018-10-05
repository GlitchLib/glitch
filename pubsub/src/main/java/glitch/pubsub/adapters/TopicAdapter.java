package glitch.pubsub.adapters;

import com.google.gson.*;
import glitch.pubsub.PubSubImpl;
import glitch.pubsub.exceptions.UnknownTopicException;
import glitch.pubsub.topics.Topic;
import java.lang.reflect.Type;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TopicAdapter implements JsonSerializer<Topic>, JsonDeserializer<Topic> {
    private final PubSubImpl pubSub;

    @Override
    public JsonElement serialize(Topic src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getRawType());
    }

    @Override
    public Topic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return pubSub.getTopics().getAll().stream().filter(topic -> topic.getRawType().equals(json.getAsString()))
                    .findFirst().orElseThrow(() -> new UnknownTopicException("Cannot find registered topic: " + json.getAsString()));
        } catch (UnknownTopicException e) {
            throw new JsonParseException(e);
        }
    }
}
