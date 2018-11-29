package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.api.http.HttpRequest;
import glitch.api.http.Unofficial;
import glitch.auth.Scope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ResponseException;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.*;
import glitch.kraken.object.json.list.EmoteSets;
import glitch.kraken.object.json.list.UserList;
import glitch.kraken.services.request.UserFollowsRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;

public class UserService extends AbstractHttpService {
    public UserService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Mono<AuthorizedUser> getUsers(Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.USER_READ)).flatMap(b -> {
            if (b) {
                return exchange(get("/user", AuthorizedUser.class).header("Authorization", "OAuth " + credential.getAccessToken())).toMono();
            } else {
                return Mono.error(handleScopeMissing(Scope.USER_READ));
            }
        });
    }

    public Mono<User> getUserById(Long id) {
        return exchange(get(String.format("/users/%s", id), User.class)).toMono();
    }

    public Flux<User> getUsers(String... logins) {
        return getUsers(Arrays.asList(logins));
    }

    public Flux<User> getUsers(Collection<String> logins) {
        return exchange(get("/users", UserList.class).queryParam("login", String.join(",", logins))).toFlux(UserList::getData);
    }

    public Flux<EmoteSet> getUserEmotes(Long id, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.USER_SUBSCRIPTIONS))
                .flatMap(b -> {
                    if (b) {
                        return exchange(get(String.format("/users/%s/emotes", id), EmoteSets.class)
                                .header("Authorization", "OAuth " + credential.getAccessToken()))
                                .toMono();
                    } else {
                        return Mono.error(handleScopeMissing(Scope.USER_SUBSCRIPTIONS));
                    }
                }).flatMapIterable(EmoteSets::toEmoteSets);
    }

    public Mono<Subscriber> checkUserSubscriptionByChannel(User user, Channel channel, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.USER_SUBSCRIPTIONS))
                .flatMap(b -> {
                    if (b) {
                        return exchange(get(String.format("/users/%s/subscriptions/%s", user.getId(), channel.getId()), Subscriber.class)
                                .header("Authorization", "OAuth " + credential.getAccessToken()))
                                .toMono()
                                .onErrorResume(ResponseException.class, (e) -> {
                                    if (e.getStatus() == 404 && e.getMessage().matches("^(.+) has no subscriptions to (.+)$")) {
                                        return Mono.empty();
                                    } else {
                                        return Mono.error(e);
                                    }
                                });
                    } else {
                        return Mono.error(handleScopeMissing(Scope.USER_SUBSCRIPTIONS));
                    }
                });
    }

    public UserFollowsRequest getUserFollows(Long id) {
        return new UserFollowsRequest(http, id);
    }

    public Mono<ChannelFollow> getFollow(User user, Channel channel) {
        return exchange(get(String.format("/users/%s/follows/channels/%s", user.getId(), channel.getId()), ChannelFollow.class)).toMono()
                .onErrorResume(ResponseException.class, (e) -> {
                    if (e.getStatus() == 404 && e.getMessage().matches("^(.+) is not following (.+)$")) {
                        return Mono.empty();
                    } else {
                        return Mono.error(e);
                    }
                });
    }

    public Mono<ChannelFollow> followChannel(User user, Channel channel, Credential credential, Boolean notifications) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.USER_FOLLOWS_EDIT))
                .flatMap(b -> {
                    if (b) {
                        HttpRequest<ChannelFollow> request = put(String.format("/users/%s/follows/channels/%s", user.getId(), channel.getId()), ChannelFollow.class)
                                .header("Authorization", "OAuth " + credential.getAccessToken());

                        if (notifications != null) {
                            request.queryParam("notifications", notifications);
                        }

                        return exchange(request).toMono();
                    } else {
                        return Mono.error(handleScopeMissing(Scope.USER_FOLLOWS_EDIT));
                    }
                });
    }

    public Mono<Boolean> unfollowChannel(User user, Channel channel, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.USER_FOLLOWS_EDIT))
                .flatMap(b -> {
                    if (b) {
                        return Mono.just(exchange(delete(String.format("/users/%s/follows/channels/%s", user.getId(), channel.getId()), ChannelFollow.class)
                                .header("Authorization", "OAuth " + credential.getAccessToken()))
                                .isSuccessful());
                    } else {
                        return Mono.error(handleScopeMissing(Scope.USER_FOLLOWS_EDIT));
                    }
                });
    }

    // TODO: Block List & VHS - https://dev.twitch.tv/docs/v5/reference/users/#get-user-block-list

    @Unofficial
    public Mono<GlobalUserState> getChatUserState(User user) {
        return getChatUserState(user.getId());
    }

    @Unofficial
    public Mono<GlobalUserState> getChatUserState(Long id) {
        return null;
    }
}
