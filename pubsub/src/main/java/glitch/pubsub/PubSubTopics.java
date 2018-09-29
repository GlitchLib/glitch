package glitch.pubsub;

import glitch.pubsub.events.Message;
import glitch.pubsub.events.TopicRegisteredEvent;
import glitch.pubsub.events.TopicUnregisteredEvent;
import glitch.pubsub.topics.Topic;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PubSubTopics {
    private final PubSubImpl client;
    private final Map<Topic, Boolean> topics = new LinkedHashMap<>(50);

    public PubSubTopics(PubSubImpl client) {
        this.client = client;
        client.listenOn(TopicRegisteredEvent.class).subscribe(event -> this.topics.replace(event.getTopic(), true));
        client.listenOn(TopicUnregisteredEvent.class).subscribe(event -> this.topics.replace(event.getTopic(), false));
    }

    public Collection<Topic> getAll() {
        return topics.keySet();
    }

    public Collection<Topic> getActive() {
        return topics.entrySet().stream().filter(Map.Entry::getValue)
                .map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    Set<Map.Entry<Topic, Boolean>> getTopicSet() {
        return topics.entrySet();
    }

    public void registerTopic(Topic topic) {
        register(topic, false);
    }
    public void registerTopics(Topic... topics) {
        registerTopics(Arrays.asList(topics));
    }
    public void registerTopics(Collection<Topic> topics) {
        topics.forEach(this::registerTopic);
    }

    public void enableTopic(Topic topic) {
        register(topic, true);
    }
    public void enableTopics(Topic... topics) {
        enableTopics(Arrays.asList(topics));
    }
    public void enableTopics(Collection<Topic> topics) {
        topics.forEach(this::enableTopic);
    }

    public void unregisterTopic(Topic topic) {
        unregister(topic, false);
    }
    public void unregisterTopics(Topic... topics) {
        unregisterTopics(Arrays.asList(topics));
    }
    public void unregisterTopics(Collection<Topic> topics) {
        topics.forEach(this::unregisterTopic);
    }

    public void disableTopic(Topic topic) {
        unregister(topic, true);
    }
    public void disableTopics(Topic... topics) {
        disableTopics(Arrays.asList(topics));
    }
    public void disableTopics(Collection<Topic> topics) {
        topics.forEach(this::disableTopic);
    }


    void sendRequest(Message.Type type, Topic topic) {
        Map<String, Object> request = new LinkedHashMap<>();
        Map<String, Object> topicData = new LinkedHashMap<>();

        topicData.put("topics", Collections.singleton(topic.getRawType()));

        if (topic.getCredential() != null) {
            topicData.put("auth_token", topic.getCredential().getAccessToken());
        }

        request.put("type", type.name());
        request.put("nonce", topic.getCode().toString());
        request.put("data", topicData);

        client.send(client.gson.toJson(request));
    }

    private void register(Topic topic, boolean activate) {
        if (!topics.containsKey(topic)) {
            topics.put(topic, false);
        }

        if (activate) {
            sendRequest(Message.Type.LISTEN, topic);
        }
    }

    public void unregister(Topic topic, boolean disable) {
        boolean active = topics.get(topic);

        if (!disable) {
            topics.remove(topic);
        }

        if (active) {
            sendRequest(Message.Type.UNLISTEN, topic);
        }
    }


}
