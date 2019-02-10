package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.Routes;
import glitch.api.objects.enums.VideoType;
import glitch.kraken.object.enums.VideoPeriod;
import glitch.kraken.object.enums.VideoSort;
import glitch.kraken.object.json.Game;
import glitch.kraken.object.json.Video;
import glitch.kraken.object.json.collections.Videos;
import glitch.service.AbstractRequest;
import java.util.*;
import java.util.stream.Collectors;
import reactor.core.publisher.Mono;

public class TopVideosRequest extends AbstractRequest<Video, Videos> {
    private final Set<VideoType> videoType = new LinkedHashSet<>();
    private final Set<Locale> language = new LinkedHashSet<>();
    private Integer limit;
    private Integer offset;
    private Game game;
    private VideoPeriod period;
    private VideoSort sort;

    public TopVideosRequest(HttpClient httpClient) {
        super(httpClient, Routes.get("/videos/top").newRequest());
    }

    public TopVideosRequest addVideoType(VideoType... types) {
        return addVideoType(Arrays.asList(types));
    }

    public TopVideosRequest addVideoType(Collection<VideoType> types) {
        videoType.addAll(types);
        return this;
    }

    public TopVideosRequest addLanguage(String... languages) {
        return addLanguage(Arrays.stream(languages).map(Locale::forLanguageTag).collect(Collectors.toSet()));
    }

    public TopVideosRequest addLanguage(Locale... languages) {
        return addLanguage(Arrays.asList(languages));
    }

    public TopVideosRequest addLanguage(Collection<Locale> languages) {
        language.addAll(languages);
        return this;
    }

    public TopVideosRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public TopVideosRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public TopVideosRequest setGame(Game game) {
        this.game = game;
        return this;
    }

    public TopVideosRequest setPeriod(VideoPeriod period) {
        this.period = period;
        return this;
    }

    public TopVideosRequest setSort(VideoSort sort) {
        this.sort = sort;
        return this;
    }

    public Mono<Videos> get() {

        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        if (game != null) {
            request.queryParam("game", game.getName());
        }

        if (period != null) {
            request.queryParam("period", period.name().toLowerCase());
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
    }
}
