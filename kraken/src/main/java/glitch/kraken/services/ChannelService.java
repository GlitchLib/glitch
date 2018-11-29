package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.api.http.HttpRequest;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.auth.Scope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ResponseException;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.*;
import glitch.kraken.object.json.list.*;
import glitch.kraken.object.json.requests.ChannelBody;
import glitch.kraken.services.request.ChannelFollowersRequest;
import glitch.kraken.services.request.ChannelSubscribersRequest;
import glitch.kraken.services.request.ChannelVideosRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

public class ChannelService extends AbstractHttpService {

    private static final Set<Integer> commercialDuration = new LinkedHashSet<>(Arrays.asList(30, 60, 90, 120, 150, 180));

    public ChannelService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Mono<AuthorizedChannel> getChannel(Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_READ)).flatMap(b -> {
            if (b) {
                return exchange(get("/channel", AuthorizedChannel.class)
                        .header("Authorization", "OAuth " + credential.getAccessToken())).toMono();
            } else {
                return Mono.error(handleScopeMissing(Scope.CHANNEL_READ));
            }
        });
    }

    public Mono<Channel> getChannel(Long id) {
        return exchange(get(String.format("/channels/%s", id), Channel.class)).toMono();
    }

    public Mono<Channel> updateChannel(Credential credential, ChannelBody data) {
        return updateChannel(credential.getUserId(), credential, data);
    }

    public Mono<Channel> updateChannel(Long id, Credential credential, ChannelBody body) {

        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_EDITOR)).flatMap(b -> {
            if (b) {
                ChannelBody channelBody = (!id.equals(credential.getUserId())) ?
                        // preventing updates if credential is not channel owner
                        body.toBuilder()
                                .delay(null)
                                .channelFeedEnabled(null)
                                .build()
                        : body;

                return exchange(put(String.format("/channels/%s", id), Channel.class)
                        .header("Authorization", "OAuth " + credential.getAccessToken())
                        .body(Collections.singletonMap("channel", channelBody))).toMono();
            } else {
                return Mono.error(handleScopeMissing(Scope.CHANNEL_EDITOR));
            }
        });
    }

    public Flux<User> getChannelEditors(Long id, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_READ))
                .flatMapMany(b -> {
                    if (b) {
                        return exchange(get(String.format("/channels/%s", id), Editors.class)
                                .header("Authorization", "OAuth " + credential.getAccessToken()))
                                .toFlux(OrdinalList::getData);
                    } else {
                        return Mono.error(handleScopeMissing(Scope.CHANNEL_READ));
                    }
                });
    }

    public ChannelFollowersRequest getChannelFollowers(Long id) {
        return new ChannelFollowersRequest(http, get(String.format("/channels/%s/follows", id), Followers.class));
    }

    public Flux<Team> getChannelTeams(Long id) {
        return exchange(get(String.format("/channels/%s/teams", id), Teams.class)).toFlux(OrdinalList::getData);
    }

    public ChannelSubscribersRequest getChannelSubscribers(Long id, Credential credential) {
        return new ChannelSubscribersRequest(http, get(String.format("/channels/%s/subscriptions", id), Subscribers.class), credential);
    }

    public Mono<Subscriber> checkChannelSubscriptionByUser(Channel channel, User user, Credential credential) {
        return checkChannelSubscriptionByUser(channel.getId(), user.getId(), credential);
    }

    public Mono<Subscriber> checkChannelSubscriptionByUser(Long channelId, Long userId, Credential credential) {

        HttpRequest<Subscriber> request = get(String.format("/channels/%s/subscriptions/%s", channelId, userId), Subscriber.class);

        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_CHECK_SUBSCRIPTION))
                .flatMap(b -> {
                    if (b) {
                        return exchange(request).toMono().onErrorResume(ResponseException.class, (e) -> {
                            if (e.getStatus() == 404 && e.getMessage().matches("^(.+) has no subscriptions to (.+)$")) {
                                return Mono.empty();
                            } else {
                                return Mono.error(e);
                            }
                        });
                    } else {
                        return Mono.error(handleScopeMissing(Scope.CHANNEL_CHECK_SUBSCRIPTION));
                    }
                });
    }

    public ChannelVideosRequest getChannelVideos(Long id) {
        return new ChannelVideosRequest(http, get(String.format("/channels/%s/videos", id), ChannelVideos.class));
    }

    public Mono<CommercialData> startChannelCommercial(Long id, Credential credential, int duration) {
        Map<String, Object> dur = Collections.singletonMap("length", commercialDuration.stream().min(Comparator.comparingInt(i -> Math.abs(i - duration))).orElse(30));

        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_COMMERCIAL))
                .flatMap(b -> {
                    if (b) {
                        return exchange(post(String.format("/channels/%s/commercial", id), CommercialData.class).body(dur)
                                .header("Authorization", "OAuth " + credential.getAccessToken())).toMono();
                    } else {
                        return Mono.error(handleScopeMissing(Scope.CHANNEL_COMMERCIAL));
                    }
                });
    }

    public Mono<Channel> resetStreamKey(Long id, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_STREAM))
                .flatMap(b -> {
                    if (b) {
                        return exchange(delete(String.format("/channels/%s/stream_key", id), Channel.class)
                                .header("Authorization", "OAuth " + credential.getAccessToken())).toMono();
                    } else {
                        return Mono.error(handleScopeMissing(Scope.CHANNEL_STREAM));
                    }
                });
    }

    public Flux<Community> getChannelCommunities(Long id) {
        return exchange(get(String.format("/channels/%s/communities", id), Communities.class))
                .toFlux(OrdinalList::getData);
    }

    public Mono<Boolean> setChannelCommunities(Long id, Credential credential, Collection<UUID> communityIds) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_EDITOR))
                .flatMap(b -> {
                    if (b) {
                        return Mono.just(exchange(put(String.format("/channels/%s/communities", id), Void.class)
                                .body(Collections.singletonMap("community_ids", communityIds.stream().map(UUID::toString).collect(Collectors.toList())))
                                .header("Authorization", "OAuth " + credential.getAccessToken())).isSuccessful());
                    } else {
                        return Mono.error(handleScopeMissing(Scope.CHANNEL_EDITOR));
                    }
                });
    }

    public Mono<Boolean> clearChannelCommunities(Long id, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_EDITOR))
                .flatMap(b -> {
                    if (b) {
                        return Mono.just(exchange(delete(String.format("/channels/%s/community", id), Void.class)
                                .header("Authorization", "OAuth " + credential.getAccessToken())).isSuccessful());
                    } else {
                        return Mono.error(handleScopeMissing(Scope.CHANNEL_EDITOR));
                    }
                });
    }
}
