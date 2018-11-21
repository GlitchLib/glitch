package glitch.pubsub;

import glitch.exceptions.GlitchException;
import glitch.pubsub.exceptions.TopicException;
import glitch.pubsub.object.enums.MessageType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.channels.NotYetConnectedException;
import java.util.*;
import java.util.stream.Collectors;

public class TopicsCache {
    private final GlitchPubSub client;
    private final Map<Topic, Boolean> topics = new LinkedHashMap<>(50);

    TopicsCache(GlitchPubSub client) {
        this.client = client;
    }

    public Collection<Topic> getAll() {
        return Collections.unmodifiableCollection(topics.keySet());
    }

    public Collection<Topic> getActive() {
        return Collections.unmodifiableCollection(topics.entrySet()
                .stream().filter(Map.Entry::getValue)
                .map(Map.Entry::getKey).collect(Collectors.toSet()));
    }

    public Mono<Void> registerTopic(Topic topic) {
        return register(topic, false);
    }

    public Mono<Void> registerTopics(Topic... topics) {
        return registerTopics(Arrays.asList(topics));
    }

    public Mono<Void> registerTopics(Collection<Topic> topics) {
        return Flux.fromIterable(topics).flatMap(this::registerTopic).then();
    }

    public Mono<Void> enableTopic(Topic topic) {
        return register(topic, true);
    }

    public Mono<Void> enableTopics(Topic... topics) {
        return enableTopics(Arrays.asList(topics));
    }

    public Mono<Void> enableTopics(Collection<Topic> topics) {
        return Flux.fromIterable(topics).flatMap(this::enableTopic).then();
    }

    public Mono<Void> unregisterTopic(Topic topic) {
        return unregister(topic, false);
    }

    public Mono<Void> unregisterTopics(Topic... topics) {
        return unregisterTopics(Arrays.asList(topics));
    }

    public Mono<Void> unregisterTopics(Collection<Topic> topics) {
        return Flux.fromIterable(topics).flatMap(this::unregisterTopic).next();
    }

    public Mono<Void> disableTopic(Topic topic) {
        return unregister(topic, true);
    }

    public Mono<Void> disableTopics(Topic... topics) {
        return disableTopics(Arrays.asList(topics));
    }

    public Mono<Void> disableTopics(Collection<Topic> topics) {
        return Flux.fromIterable(topics).flatMap(this::disableTopic).then();
    }

    Mono<Void> register(Topic topic, boolean activate) {
        if (!topics.containsKey(topic)) {
            topics.put(topic, false);
        }

        return (activate) ? exchange(MessageType.LISTEN, topic)
                .doOnSuccess(ignore -> topics.replace(topic, true)) : Mono.empty();
    }

    Mono<Void> unregister(Topic topic, boolean disable) {
        if (topics.containsKey(topic)) {
            return Mono.just(topics.get(topic))
                    .flatMap(active -> (active) ? exchange(MessageType.UNLISTEN, topic) : Mono.error(new TopicException("This topic is already disabled: " + topic.getRawType())))
                    .doOnSuccess(ignore -> {
                        if (!disable) {
                            topics.remove(topic);
                        } else {
                            topics.replace(topic, false);
                        }
                    });
        } else return Mono.error(new TopicException("Topic is not registered or it is not exist: " + topic.getRawType()));
    }


    Mono<Void> exchange(MessageType type, Topic topic) {
        if (client.isOpen()) {
            return client.buildMessage(type, topic);
        } else return Mono.error(new GlitchException("Cannot send message!", new NotYetConnectedException()));
    }

}
