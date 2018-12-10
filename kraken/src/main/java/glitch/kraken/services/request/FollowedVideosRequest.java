package glitch.kraken.services.request;

import glitch.api.AbstractRequest;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.auth.Scope;
import glitch.auth.objects.json.Credential;
import glitch.kraken.object.enums.VideoSort;
import glitch.kraken.object.json.Video;
import glitch.kraken.object.json.list.Videos;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Setter
@Accessors(chain = true, fluent = true)
public class FollowedVideosRequest extends AbstractRequest<Videos, Video> {
    private final Credential credential;

    private Integer limit;
    private Integer offset;
    private final Set<Video.Type> videoType = new LinkedHashSet<>();
    private final Set<Locale> language = new LinkedHashSet<>();
    private VideoSort sort;

    public FollowedVideosRequest(GlitchHttpClient httpClient, HttpRequest<Videos> request, Credential credential) {
        super(httpClient, request);
        this.credential = credential;
    }

    public FollowedVideosRequest videoType(Video.Type... types) {
        return videoType(Arrays.asList(types));
    }

    public FollowedVideosRequest videoType(Collection<Video.Type> types) {
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

    @Override
    protected HttpResponse<Videos> exchange() {

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

        return httpClient.exchange(request);
    }

    public Mono<Videos> get() {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.USER_READ))
                .flatMap(b -> {
                    if (b) {
                        return exchange().toMono();
                    } else {
                        return Mono.error(handleScopeMissing(Scope.USER_READ));
                    }
                });
    }

    public Flux<Video> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
