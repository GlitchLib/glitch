package glitch.pubsub.object.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import glitch.pubsub.GlitchPubSub;
import glitch.pubsub.Topic;
import glitch.pubsub.exceptions.UnknownTopicException;
import java.io.IOException;

public class TopicHandler extends TypeAdapter<Topic> {
    private final GlitchPubSub pubSub;

    @java.beans.ConstructorProperties({"pubSub"})
    public TopicHandler(GlitchPubSub pubSub) {
        this.pubSub = pubSub;
    }

    @Override
    public void write(JsonWriter out, Topic value) throws IOException {
        out.value(value.getRawType());
    }

    @Override
    public Topic read(JsonReader in) throws IOException {
        Topic raw = Topic.fromRaw(in.nextString());
        try {
            return pubSub.getTopicsCache().getAll().stream()
                    .filter(t -> t.getRawType().equals(raw.getRawType())).findFirst()
                    .orElseThrow(() -> new UnknownTopicException("Cannot find a registered topic"));
        } catch (UnknownTopicException e) {
            throw new IOException(e);
        }
    }
}
