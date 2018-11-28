package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.auth.Scope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ResponseException;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.enums.Direction;
import glitch.kraken.object.enums.VideoSort;
import glitch.kraken.object.json.*;
import glitch.kraken.object.json.list.*;
import glitch.kraken.object.json.requests.ChannelData;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
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

    public Mono<Channel> updateChannel(Credential credential, Consumer<ChannelData> data) {
        return updateChannel(credential.getUserId(), credential, data);
    }

    public Mono<Channel> updateChannel(Long id, Credential credential, Consumer<ChannelData> dataConsume) {
        ChannelData data = new ChannelData();
        dataConsume.accept(data);
        if (!id.equals(credential.getUserId())) {
            // preventing updates if credential is not channel owner
            data.setDelay(null);
            data.setChannelFeedEnabled(null);
        }

        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_EDITOR)).flatMap(b -> {
            if (b) {
                return exchange(put(String.format("/channels/%s", id), Channel.class)
                        .header("Authorization", "OAuth " + credential.getAccessToken())
                        .body(Collections.singletonMap("channel", data))).toMono();
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

    public Mono<Followers> getChannelFollowers(Long id, int limit, int offset, @Nullable String cursor, Direction direction) {
        // prevents negative data
        if (limit < 0) {
            limit = 25;
        }

        if (offset < 0) {
            offset = 0;
        }

        // prevents max limit to 100
        if (limit > 100) {
            limit = 100;
        }

        // setting default direction (desc) if it is empty
        if (direction == null) {
            direction = Direction.DESC;
        }

        HttpRequest<Followers> request = get(String.format("/channels/%s/follows", id), Followers.class)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .queryParam("direction", direction.name().toLowerCase());

        if (cursor != null) {
            request.queryParam("cursor", cursor);
        }

        return exchange(request).toMono();
    }

    public Flux<Team> getChannelTeams(Long id) {
        return exchange(get(String.format("/channels/%s/teams", id), Teams.class)).toFlux(OrdinalList::getData);
    }

    public Mono<Subscribers> getChannelSubscribers(Long id, Credential credential, int limit, int offset, Direction direction) {
        // prevents negative data
        if (limit < 0) {
            limit = 25;
        }

        if (offset < 0) {
            offset = 0;
        }

        // prevents max limit to 100
        if (limit > 100) {
            limit = 100;
        }

        // setting default direction (desc) if it is empty
        if (direction == null) {
            direction = Direction.ASC;
        }

        HttpRequest<Subscribers> request = get(String.format("/channels/%s/subscriptions", id), Subscribers.class)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .queryParam("direction", direction.name().toLowerCase());

        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_SUBSCRIPTIONS))
                .flatMap(b -> {
                    if (b) {
                        return exchange(request).toMono();
                    } else {
                        return Mono.error(handleScopeMissing(Scope.CHANNEL_SUBSCRIPTIONS));
                    }
                });
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

    public Mono<ChannelVideos> getChannelVideos(Long id, int limit, int offset, Collection<Video.Type> videoType, Collection<Locale> languages, VideoSort sort) {
        // prevents negative data
        if (limit < 0) {
            limit = 25;
        }

        if (offset < 0) {
            offset = 0;
        }

        // prevents max limit to 100
        if (limit > 100) {
            limit = 100;
        }

        if (sort == null) {
            sort = VideoSort.TIME;
        }

        HttpRequest<ChannelVideos> request = get(String.format("/channels/%s/videos", id), ChannelVideos.class)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .queryParam("sort", sort.name().toLowerCase());

        if (videoType != null && videoType.size() > 0) {
            request.queryParam("broadcast_type", videoType.stream().map(v -> v.name().toLowerCase()).collect(Collectors.joining(",")));
        }

        if (languages != null && languages.size() > 0) {
            request.queryParam("language", languages.stream().map(Locale::getLanguage).collect(Collectors.joining(",")));
        }

        return exchange(request).toMono();
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

    public Mono<HttpResponse<Void>> setChannelCommunities(Long id, Credential credential, Collection<UUID> communityIds) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_EDITOR))
                .flatMap(b -> {
                    if (b) {
                        return Mono.just(exchange(put(String.format("/channels/%s/communities", id), Void.class)
                                .body(Collections.singletonMap("community_ids", communityIds.stream().map(UUID::toString).collect(Collectors.toList())))
                                .header("Authorization", "OAuth " + credential.getAccessToken())));
                    } else {
                        return Mono.error(handleScopeMissing(Scope.CHANNEL_EDITOR));
                    }
                });
    }

    public Mono<HttpResponse<Void>> clearChannelCommunities(Long id, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_EDITOR))
                .flatMap(b -> {
                    if (b) {
                        return Mono.just(exchange(delete(String.format("/channels/%s/community", id), Void.class)
                                .header("Authorization", "OAuth " + credential.getAccessToken())));
                    } else {
                        return Mono.error(handleScopeMissing(Scope.CHANNEL_EDITOR));
                    }
                });
    }
}
