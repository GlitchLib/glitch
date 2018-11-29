package glitch.kraken.services.request;

import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.kraken.object.enums.VideoSort;
import glitch.kraken.object.json.Video;
import glitch.kraken.object.json.list.ChannelVideos;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Setter
@Accessors(chain = true, fluent = true)
public class ChannelVideosRequest extends AbstractRequest<ChannelVideos, Video> {
    private Integer limit;
    private Integer offset;
    private final Set<Video.Type> videoType = new LinkedHashSet<>();
    private final Set<Locale> languages = new LinkedHashSet<>();
    private VideoSort sort;

    public ChannelVideosRequest(GlitchHttpClient httpClient, HttpRequest<ChannelVideos> request) {
        super(httpClient, request);
    }

    public ChannelVideosRequest videoType(Video.Type... types) {
        return videoType(Arrays.asList(types));
    }

    public ChannelVideosRequest videoType(Collection<Video.Type> types) {
        videoType.addAll(types);
        return this;
    }

    public ChannelVideosRequest language(String... languages) {
        return language(Arrays.stream(languages).map(Locale::forLanguageTag).collect(Collectors.toSet()));
    }

    public ChannelVideosRequest language(Locale... languages) {
        return language(Arrays.asList(languages));
    }

    public ChannelVideosRequest language(Collection<Locale> languages) {
        this.languages.addAll(languages);
        return this;
    }

    @Override
    protected HttpResponse<ChannelVideos> exchange() {
        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        if (sort != null) {
            request.queryParam("sort", sort.name().toLowerCase());
        }

        if (!videoType.isEmpty()) {
            request.queryParam("broadcast_type", videoType.stream().map(v -> v.name().toLowerCase()).collect(Collectors.joining(",")));
        }

        if (!languages.isEmpty()) {
            request.queryParam("language", languages.stream().map(Locale::getLanguage).collect(Collectors.joining(",")));
        }

        return httpClient.exchange(request);
    }

    @Override
    public Mono<ChannelVideos> get() {
        return null;
    }

    @Override
    public Flux<Video> getIterable() {
        return null;
    }
}
