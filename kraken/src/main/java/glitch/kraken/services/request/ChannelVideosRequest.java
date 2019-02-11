package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.api.objects.enums.VideoType;
import glitch.kraken.object.enums.VideoSort;
import glitch.kraken.object.json.Video;
import glitch.kraken.object.json.collections.Videos;
import glitch.service.AbstractRequest;
import java.util.*;
import java.util.stream.Collectors;
import reactor.core.publisher.Mono;

public class ChannelVideosRequest extends AbstractRequest<Video, Videos> {
    private final Set<VideoType> videoType = new LinkedHashSet<>();
    private final Set<Locale> languages = new LinkedHashSet<>();
    private Integer limit;
    private Integer offset;
    private VideoSort sort;

    public ChannelVideosRequest(HttpClient httpClient, Long id) {
        super(httpClient, Routes.get("/channels/%s/videos").newRequest(id));
    }

    public ChannelVideosRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public ChannelVideosRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public ChannelVideosRequest setSort(VideoSort sort) {
        this.sort = sort;
        return this;
    }

    public ChannelVideosRequest addVideoType(VideoType... types) {
        return addVideoType(Arrays.asList(types));
    }

    public ChannelVideosRequest addVideoType(Collection<VideoType> types) {
        videoType.addAll(types);
        return this;
    }

    public ChannelVideosRequest addLanguage(String... languages) {
        return addLanguage(Arrays.stream(languages).map(Locale::forLanguageTag).collect(Collectors.toSet()));
    }

    public ChannelVideosRequest addLanguage(Locale... languages) {
        return addLanguage(Arrays.asList(languages));
    }

    public ChannelVideosRequest addLanguage(Collection<Locale> languages) {
        this.languages.addAll(languages);
        return this;
    }

    @Override
    public Mono<Videos> get() {

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

        return httpClient.exchangeAs(request, Videos.class);
    }
}
