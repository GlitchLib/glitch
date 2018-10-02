package glitch.pubsub;

import glitch.pubsub.events.Message;
import glitch.pubsub.events.TopicRegisteredEvent;
import glitch.pubsub.events.TopicUnregisteredEvent;
import glitch.pubsub.topics.Topic;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.nio.channels.NotYetConnectedException;
import java.util.*;
import java.util.stream.Collectors;

public class PubSubTopics {
    private final PubSubImpl client;
    private final Map<Topic, Boolean> topics = new LinkedHashMap<>(50);

    PubSubTopics(PubSubImpl client) {
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

    public Single<Void> registerTopic(Topic topic) {
        return register(topic, false);
    }

    public Single<Void> registerTopics(Topic... topics) {
        return registerTopics(Arrays.asList(topics));
    }

    public Single<Void> registerTopics(Collection<Topic> topics) {
        return Observable.fromIterable(topics).flatMapSingle(this::registerTopic).singleOrError();
    }

    public Single<Void> enableTopic(Topic topic) {
        return register(topic, true);
    }

    public Single<Void> enableTopics(Topic... topics) {
        return enableTopics(Arrays.asList(topics));
    }

    public Single<Void> enableTopics(Collection<Topic> topics) {
        return Observable.fromIterable(topics).flatMapSingle(this::enableTopic).singleOrError();
    }

    public Single<Void> unregisterTopic(Topic topic) {
        return unregister(topic, false);
    }

    public Single<Void> unregisterTopics(Topic... topics) {
        return unregisterTopics(Arrays.asList(topics));
    }

    public Single<Void> unregisterTopics(Collection<Topic> topics) {
        return Observable.fromIterable(topics).flatMapSingle(this::unregisterTopic).singleOrError();
    }

    public Single<Void> disableTopic(Topic topic) {
        return unregister(topic, true);
    }

    public Single<Void> disableTopics(Topic... topics) {
        return disableTopics(Arrays.asList(topics));
    }

    public Single<Void> disableTopics(Collection<Topic> topics) {
        return Observable.fromIterable(topics).flatMapSingle(this::disableTopic).singleOrError();
    }

    Single<Void> register(Topic topic, boolean activate) {
        if (!topics.containsKey(topic)) {
            topics.put(topic, false);
        }

        return exchange(Message.Type.LISTEN, topic, activate);
    }

    Single<Void> unregister(Topic topic, boolean disable) {
        if (topics.containsKey(topic)) {
            return Single.just(topics.get(topic)).flatMap(active -> exchange(Message.Type.UNLISTEN, topic, active))
                    .flatMap(ignore -> {
                        if (!disable) {
                            topics.remove(topic);
                        }
                        return Single.never();
                    });
        } else return Single.never();
    }

    Single<Void> exchange(Message.Type type, Topic topic, boolean active) {
        return client.isActive().flatMap(ws -> {
            if (active) {
                if (ws) {
                    return client.sendRequest(Message.Type.UNLISTEN, topic);
                } else return Single.error(new NotYetConnectedException());
            } else return Single.never();
        });
    }
}
