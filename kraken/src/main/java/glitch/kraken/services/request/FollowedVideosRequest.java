package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.Routes;
import glitch.api.objects.enums.VideoType;
import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Credential;
import glitch.kraken.object.enums.VideoSort;
import glitch.kraken.object.json.Video;
import glitch.kraken.object.json.collections.Videos;
import glitch.service.AbstractRequest;
import java.util.*;
import java.util.stream.Collectors;
import reactor.core.publisher.Mono;

public class FollowedVideosRequest extends AbstractRequest<Video, Videos> {
    private final Credential credential;
    private final Set<VideoType> videoType = new LinkedHashSet<>();
    private final Set<Locale> language = new LinkedHashSet<>();
    private Integer limit;
    private Integer offset;
    private VideoSort sort;

    public FollowedVideosRequest(HttpClient httpClient, Credential credential) {
        super(httpClient, Routes.get("/videos/followed").newRequest());
        this.credential = credential;
    }

    public FollowedVideosRequest videoType(VideoType... types) {
        return videoType(Arrays.asList(types));
    }

    public FollowedVideosRequest videoType(Collection<VideoType> types) {
        videoType.addAll(types);
        return this;
    }

    public FollowedVideosRequest language(String... languages) {
        return language(Arrays.stream(languages).map(Locale::forLanguageTag).collect(Collectors.toSet()));
    }

    public FollowedVideosRequest language(Locale... languages) {
        return language(Arrays.asList(languages));
    }

    public FollowedVideosRequest language(Collection<Locale> languages) {
        language.addAll(languages);
        return this;
    }

    public Mono<Videos> get() {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.USER_READ))
                .flatMap(b -> {
                    if (b) {

                        if (limit != null && limit > 0 && limit <= 100) {
                            request.queryParam("limit", limit);
                        }

                        if (offset != null && offset >= 0) {
                            request.queryParam("offset", offset);
                        }

                        if (!videoType.isEmpty()) {
                            request.queryParam("broadcast_type", videoType.stream().map(Enum::name).collect(Collectors.joining(",")).toLowerCase());
                        }

                        if (!language.isEmpty()) {
                            request.queryParam("language", language.stream().map(Locale::getLanguage).collect(Collectors.joining(",")).toLowerCase());
                        }

                        if (sort != null) {
                            request.queryParam("sort", sort.name().toLowerCase());
                        }

                        return httpClient.exchangeAs(request, Videos.class);
                    } else {
                        return Mono.error(handleScopeMissing(GlitchScope.USER_READ));
                    }
                });
    }
}
