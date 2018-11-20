package glitch.chat.object.entities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import glitch.api.objects.json.Badge;
import glitch.chat.GlitchChat;
import glitch.chat.events.ChannelMessageEvent;
import glitch.chat.events.PartUserChannelEvent;
import glitch.chat.exceptions.UserNotFoundException;
import glitch.chat.object.json.RoomState;
import glitch.kraken.object.json.Channel;
import glitch.kraken.services.ChannelService;
import glitch.kraken.services.UserService;
import lombok.Getter;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.Collectors;

@Getter
public class ChannelEntity implements AbstractEntity<Channel> {
    private final GlitchChat client;
    private final Mono<Channel> data;
    private final String channelName;

    private RoomState roomState;

    private final Multimap<String, Badge> users = MultimapBuilder.linkedHashKeys().hashSetValues().build();

    public ChannelEntity(GlitchChat client, String channelName) {
        this.client = client;
        this.data = (client.getApi() != null) ? client.getApi().use(UserService.class).flatMap(service -> service.getUser(channelName).next())
                .flatMap(u -> client.getApi().use(ChannelService.class).flatMap(service -> service.getChannel(u.getId()))).switchIfEmpty(Mono.empty()) : Mono.empty();
        this.channelName = channelName;

        registerUsers();
    }

    public Mono<Void> sendMessage(Publisher<String> message) {
        return getBot().flatMap(user -> client.getConfiguration().getChatLimits(user.isModerator()))
                .flatMap(delay -> client.send(Flux.from(message)
                        .map(m -> String.format("PRIVMSG #%s %s", channelName, (m.startsWith("/")) ? m : ":" + m)))
                        .delayElement(Duration.ofMillis(delay)));
    }

    public ImmutableList<ChannelUserEntity> getUsers() {
        return ImmutableList.copyOf(users.asMap().entrySet().stream().map(e -> new ChannelUserEntity(this, e.getKey(), e.getValue())).collect(Collectors.toList()));
    }

    public ImmutableList<ChannelUserEntity> getModerators() {
        return ImmutableList.copyOf(getUsers().stream().filter(ChannelUserEntity::isModerator).collect(Collectors.toList()));
    }

    public ImmutableList<ChannelUserEntity> getSubscribers() {
        return ImmutableList.copyOf(getUsers().stream().filter(ChannelUserEntity::isModerator).collect(Collectors.toList()));
    }

    public ImmutableList<ChannelUserEntity> getVips() {
        return ImmutableList.copyOf(getUsers().stream().filter(ChannelUserEntity::isModerator).collect(Collectors.toList()));
    }

    public ImmutableList<ChannelUserEntity> getEditors() {
        return ImmutableList.copyOf(getUsers().stream().filter(ChannelUserEntity::isModerator).collect(Collectors.toList()));
    }

    public Mono<ChannelUserEntity> getUser(String user) {
        return Mono.create(sink -> {
            if (users.containsKey(user)) {
                sink.success(new ChannelUserEntity(this, user, users.get(user)));
            } else {
                sink.error(new UserNotFoundException("User is not in channel"));
            }
        });
    }

    public Mono<Void> leave() {
        return this.getClient().send(Mono.just(String.format("PART #%s", channelName)))
                .doOnSuccess(ignore -> this.getClient().getChannels().remove(this));
    }

    public ModerationEntity moderate() {
        return new ModerationEntity(this);
    }

    private void registerUsers() {
        client.listenOn(ChannelMessageEvent.class)
                .subscribe(event -> event.getUser().subscribe(user -> {
                    if (!users.containsKey(user.getUsername())) {
                        if (!users.get(user.getUsername()).containsAll(user.getChannelBadges())) {
                            users.replaceValues(user.getUsername(), user.getChannelBadges());
                        }
                    } else {
                        users.putAll(user.getUsername(), user.getChannelBadges());
                    }
                }));
        client.listenOn(PartUserChannelEvent.class)
                .subscribe(event -> event.getUser().subscribe(user -> {
                   if (users.containsKey(user.getUsername())) {
                       users.removeAll(user.getUsername());
                   }
                }));
    }

    public Mono<ChannelUserEntity> getBot() {
        return client.getBot().flatMap(user -> getUser(user.getUsername()));
    }
}
