package glitch.kraken.services;

import glitch.api.http.HttpRequest;
import glitch.api.http.Routes;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ResponseException;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.*;
import glitch.kraken.object.json.collections.EmoteSets;
import glitch.kraken.object.json.collections.Users;
import glitch.kraken.object.json.impl.AuthUserImpl;
import glitch.kraken.services.request.UserFollowsRequest;
import glitch.service.AbstractHttpService;
import java.util.Arrays;
import java.util.Collection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserService extends AbstractHttpService {
    public UserService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Mono<AuthUser> getUsers(Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.USER_READ)).flatMap(b -> {
            if (b) {
                return exchangeTo(Routes.get("/user").newRequest()
                        .header("Authorization", "OAuth " + credential.getAccessToken()), AuthUserImpl.class);
            } else {
                return Mono.error(handleScopeMissing(GlitchScope.USER_READ));
            }
        });
    }

    public Mono<User> getUserById(Long id) {
        return exchangeTo(Routes.get("/users/%s").newRequest(id), User.class);
    }

    public Flux<User> getUsers(String... logins) {
        return getUsers(Arrays.asList(logins));
    }

    public Flux<User> getUsers(Collection<String> logins) {
        return exchangeTo(Routes.get("/users").newRequest()
                .queryParam("login", String.join(",", logins)), Users.class).flatMapIterable(OrdinalList::getData);
    }

    public Flux<EmoteSet> getUserEmotes(Long id, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.USER_SUBSCRIPTIONS))
                .flatMap(b -> {
                    if (b) {
                        return exchangeTo(Routes.get("/users/%s/emotes").newRequest(id)
                                .header("Authorization", "OAuth " + credential.getAccessToken()), EmoteSets.class);
                    } else {
                        return Mono.error(handleScopeMissing(GlitchScope.USER_SUBSCRIPTIONS));
                    }
                }).flatMapIterable(EmoteSets::toEmoteSets);
    }

    public Mono<Subscriber> checkUserSubscriptionByChannel(User user, Channel channel, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.USER_SUBSCRIPTIONS))
                .flatMap(b -> {
                    if (b) {
                        return exchangeTo(Routes.get("/users/%s/subscriptions/%s").newRequest(user.getId(), channel.getId())
                                .header("Authorization", "OAuth " + credential.getAccessToken()), Subscriber.class)
                                .onErrorResume(ResponseException.class, (e) -> {
                                    if (e.getStatus() == 404 && e.getMessage().matches("^(.+) has no subscriptions to (.+)$")) {
                                        return Mono.empty();
                                    } else {
                                        return Mono.error(e);
                                    }
                                });
                    } else {
                        return Mono.error(handleScopeMissing(GlitchScope.USER_SUBSCRIPTIONS));
                    }
                });
    }

    public UserFollowsRequest getUserFollows(Long id) {
        return new UserFollowsRequest(http, id);
    }

    public Mono<ChannelUserFollow> getFollow(User user, Channel channel) {
        return exchangeTo(Routes.get("/users/%s/follows/channels/%s").newRequest(user.getId(), channel.getId()), ChannelUserFollow.class)
                .onErrorResume(ResponseException.class, (e) -> {
                    if (e.getStatus() == 404 && e.getMessage().matches("^(.+) is not following (.+)$")) {
                        return Mono.empty();
                    } else {
                        return Mono.error(e);
                    }
                });
    }

    public Mono<ChannelUserFollow> followChannel(User user, Channel channel, Credential credential, Boolean notifications) {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.USER_FOLLOWS_EDIT))
                .flatMap(b -> {
                    if (b) {
                        HttpRequest request = Routes.put("/users/%s/follows/channels/%s").newRequest(user.getId(), channel.getId())
                                .header("Authorization", "OAuth " + credential.getAccessToken());

                        if (notifications != null) {
                            request.queryParam("notifications", notifications);
                        }

                        return exchangeTo(request, ChannelUserFollow.class);
                    } else {
                        return Mono.error(handleScopeMissing(GlitchScope.USER_FOLLOWS_EDIT));
                    }
                });
    }

    public Mono<Boolean> unfollowChannel(User user, Channel channel, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.USER_FOLLOWS_EDIT))
                .flatMap(b -> {
                    if (b) {
                        return exchange(Routes.delete("/users/%s/follows/channels/%s").newRequest(user.getId(), channel.getId())
                                .header("Authorization", "OAuth " + credential.getAccessToken()))
                                .map(r -> r.getStatus().getCode() == 204);
                    } else {
                        return Mono.error(handleScopeMissing(GlitchScope.USER_FOLLOWS_EDIT));
                    }
                });
    }

    // TODO: Block List & VHS - https://dev.twitch.tv/docs/v5/reference/users/#get-user-block-list
}
