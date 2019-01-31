package glitch.kraken.services;

import glitch.api.http.HttpRequest;
import glitch.api.http.Routes;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ResponseException;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.*;
import glitch.kraken.object.json.collections.Communities;
import glitch.kraken.object.json.collections.Editors;
import glitch.kraken.object.json.collections.Teams;
import glitch.kraken.object.json.impl.AuthChannelImpl;
import glitch.kraken.services.request.ChannelSubscribersRequest;
import glitch.kraken.services.request.ChannelUserFollowsRequest;
import glitch.kraken.services.request.ChannelVideosRequest;
import glitch.service.AbstractHttpService;
import java.util.*;
import java.util.stream.Collectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ChannelService extends AbstractHttpService {

    private static final Set<Integer> commercialDuration = new LinkedHashSet<>(Arrays.asList(30, 60, 90, 120, 150, 180));

    public ChannelService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Mono<AuthChannel> getChannel(Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.CHANNEL_READ)).flatMap(b -> {
            if (b) {
                return exchangeTo(Routes.get("/channel").newRequest()
                                .header("Authorization", "OAuth " + credential.getAccessToken()),
                        AuthChannelImpl.class)
                        .cast(AuthChannel.class);
            } else {
                return Mono.error(handleScopeMissing(GlitchScope.CHANNEL_READ));
            }
        });
    }

    public Mono<Channel> getChannel(Long id) {
        return exchangeTo(Routes.get("/channels/%s").newRequest(id), Channel.class);
    }

    public Mono<Channel> updateChannel(Credential credential, ChannelBody data) {
        return updateChannel(credential.getUserId(), credential, data);
    }

    public Mono<Channel> updateChannel(Long id, Credential credential, ChannelBody body) {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.CHANNEL_EDITOR)).flatMap(b -> {
            if (b) {
                ChannelBody channelBody = (!id.equals(credential.getUserId())) ?
                        // preventing updates if credential is not channel owner
                        ChannelBody.builder(body)
                                .setDelay(null)
                                .setChannelFeedEnabled(null)
                                .build()
                        : body;

                return exchangeTo(Routes.put("/channels/%s").newRequest(id)
                        .header("Authorization", "OAuth " + credential.getAccessToken())
                        .body(HttpRequest.BodyType.JSON, Collections.singletonMap("channel", channelBody)), Channel.class);
            } else {
                return Mono.error(handleScopeMissing(GlitchScope.CHANNEL_EDITOR));
            }
        });
    }

    public Flux<User> getChannelEditors(Long id, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.CHANNEL_READ))
                .flatMapMany(b -> {
                    if (b) {
                        return exchangeTo(Routes.get("/channels/%s").newRequest(id)
                                .header("Authorization", "OAuth " + credential.getAccessToken()), Editors.class)
                                .flatMapIterable(OrdinalList::getData);
                    } else {
                        return Mono.error(handleScopeMissing(GlitchScope.CHANNEL_READ));
                    }
                });
    }

    public ChannelUserFollowsRequest getChannelFollowers(Long id) {
        return new ChannelUserFollowsRequest(http, id);
    }

    public Flux<Team> getChannelTeams(Long id) {
        return exchangeTo(Routes.get("/channels/%s/teams").newRequest(id), Teams.class)
                .flatMapIterable(OrdinalList::getData);
    }

    public ChannelSubscribersRequest getChannelSubscribers(Long id, Credential credential) {
        return new ChannelSubscribersRequest(http, id, credential);
    }

    public Mono<Subscriber> checkChannelSubscriptionByUser(Channel channel, User user, Credential credential) {
        return checkChannelSubscriptionByUser(channel.getId(), user.getId(), credential);
    }

    public Mono<Subscriber> checkChannelSubscriptionByUser(Long channelId, Long userId, Credential credential) {

        HttpRequest request = Routes.get("/channels/%s/subscriptions/%s").newRequest(channelId, userId);

        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.CHANNEL_CHECK_SUBSCRIPTION))
                .flatMap(b -> {
                    if (b) {
                        return exchangeTo(request, Subscriber.class).onErrorResume(ResponseException.class, (e) -> {
                            if (e.getStatus() == 404 && e.getStatusMessage().matches("^(.+) has no subscriptions to (.+)$")) {
                                return Mono.error(new NullPointerException(e.getStatusMessage()));
                            } else {
                                return Mono.error(e);
                            }
                        });
                    } else {
                        return Mono.error(handleScopeMissing(GlitchScope.CHANNEL_CHECK_SUBSCRIPTION));
                    }
                });
    }

    public ChannelVideosRequest getChannelVideos(Long id) {
        return new ChannelVideosRequest(http, id);
    }

    public Mono<CommercialData> startChannelCommercial(Long id, Credential credential, int duration) {
        Map<String, Object> dur = Collections.singletonMap("length", commercialDuration.stream().min(Comparator.comparingInt(i -> Math.abs(i - duration))).orElse(30));

        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.CHANNEL_COMMERCIAL))
                .flatMap(b -> {
                    if (b) {
                        return exchangeTo(Routes.post("/channels/%s/commercial").newRequest(id).body(HttpRequest.BodyType.JSON, dur)
                                .header("Authorization", "OAuth " + credential.getAccessToken()), CommercialData.class);
                    } else {
                        return Mono.error(handleScopeMissing(GlitchScope.CHANNEL_COMMERCIAL));
                    }
                });
    }

    public Mono<AuthChannel> resetStreamKey(Long id, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.CHANNEL_STREAM))
                .flatMap(b -> {
                    if (b) {
                        return exchangeTo(Routes.delete("/channels/%s/stream_key").newRequest(id)
                                .header("Authorization", "OAuth " + credential.getAccessToken()), AuthChannel.class);
                    } else {
                        return Mono.error(handleScopeMissing(GlitchScope.CHANNEL_STREAM));
                    }
                });
    }

    public Flux<Community> getChannelCommunities(Long id) {
        return exchangeTo(Routes.get("/channels/%s/communities").newRequest(id), Communities.class)
                .flatMapIterable(OrdinalList::getData);
    }

    public Mono<Boolean> setChannelCommunities(Long id, Credential credential, Collection<UUID> communityIds) {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.CHANNEL_EDITOR))
                .flatMap(b -> {
                    if (b) {
                        return exchange(Routes.put("/channels/%s/communities").newRequest(id)
                                .body(HttpRequest.BodyType.JSON, Collections.singletonMap("community_ids", communityIds.stream().map(UUID::toString).collect(Collectors.toList())))
                                .header("Authorization", "OAuth " + credential.getAccessToken()))
                                .map(response -> response.getStatus().getCode() == 204);
                    } else {
                        return Mono.error(handleScopeMissing(GlitchScope.CHANNEL_EDITOR));
                    }
                });
    }

    public Mono<Boolean> clearChannelCommunities(Long id, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.CHANNEL_EDITOR))
                .flatMap(b -> {
                    if (b) {
                        return exchange(Routes.delete("/channels/%s/community").newRequest(id)
                                .header("Authorization", "OAuth " + credential.getAccessToken()))
                                .map(response -> response.getStatus().getCode() == 204);
                    } else {
                        return Mono.error(handleScopeMissing(GlitchScope.CHANNEL_EDITOR));
                    }
                });
    }
}
