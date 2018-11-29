package glitch.chat.object.entities;

import glitch.api.objects.json.Badge;
import glitch.chat.GlitchChat;
import glitch.kraken.object.json.User;
import glitch.kraken.services.UserService;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Collection;

@Data
public class ChannelUserEntity {

    private final ChannelEntity channel;
    private final String username;
    private final Collection<Badge> channelBadges;

    public boolean isModerator() {
        return channelBadges.stream().anyMatch(b -> b.getName().equals("moderator"));
    }

    public boolean isSubscriber() {
        return channelBadges.stream().anyMatch(b -> b.getName().equals("subscriber"));
    }

    public boolean isVip() {
        return channelBadges.stream().anyMatch(b -> b.getName().equals("vip"));
    }

    public boolean isGlobalModerator() {
        return channelBadges.stream().anyMatch(b -> b.getName().equals("global_mod"));
    }

    public boolean isTwitchStaff() {
        return channelBadges.stream().anyMatch(b -> b.getName().equals("staff"));
    }

    public boolean isTwitchAdmin() {
        return channelBadges.stream().anyMatch(b -> b.getName().equals("admin"));
    }

    public boolean hasTurbo() {
        return channelBadges.stream().anyMatch(b -> b.getName().equals("turbo"));
    }

    public boolean hasPrime() {
        return channelBadges.stream().anyMatch(b -> b.getName().equals("premium"));
    }

    public boolean isBroadcaster() {
        return channelBadges.stream().anyMatch(b -> b.getName().equals("broadcaster"));
    }

    public GlitchChat getClient() {
        return channel.getClient();
    }

    public Mono<User> getData() {
        return (getClient().getApi() != null) ? getClient().getApi().use(UserService.class).flatMap(service -> service.getUsers(username).next()).switchIfEmpty(Mono.empty()) : Mono.empty();
    }

    public Mono<Long> getFollowTime() {
        return (getClient().getApi() != null) ? getData().zipWith(channel.getData())
                .flatMap(u -> getClient().getApi().use(UserService.class).flatMap(service -> service.getFollow(u.getT1(), u.getT2())))
                .map(f -> Instant.now().getEpochSecond() - f.getCreatedAt().getEpochSecond())
                .onErrorReturn(0L)
                : Mono.empty();
    }

    public Mono<Boolean> isFollowing() {
        return getFollowTime().map(time -> time > 0L);
    }

    public UserEntity createDM() {
        return new UserEntity(channel.getClient(), username);
    }
}
