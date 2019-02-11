package glitch.helix.service.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.api.objects.enums.VideoType;
import glitch.helix.object.enums.Period;
import glitch.helix.object.enums.VideoSort;
import glitch.helix.object.json.Game;
import glitch.helix.object.json.User;
import glitch.helix.object.json.Video;
import glitch.helix.object.json.Videos;
import glitch.service.AbstractRequest;
import java.util.Locale;
import reactor.core.publisher.Mono;

public class VideoRequest extends AbstractRequest<Video, Videos> {

    private String after;
    private String before;
    private Integer first;
    private Locale language;
    private Period period;
    private VideoSort sort;
    private VideoType type;

    public VideoRequest(HttpClient httpClient, User user) {
        super(httpClient, Routes.get("/videos").newRequest().queryParam("user_id", user.getId()));
    }

    public VideoRequest(HttpClient httpClient, Game game) {
        super(httpClient, Routes.get("/videos").newRequest().queryParam("user_id", game.getId()));
    }

    public VideoRequest setAfter(String after) {
        this.after = after;
        this.before = null;
        return this;
    }

    public VideoRequest setBefore(String before) {
        this.before = before;
        this.after = null;
        return this;
    }

    public VideoRequest setFirst(Integer first) {
        this.first = first;
        return this;
    }

    public VideoRequest setLanguage(Locale language) {
        this.language = language;
        return this;
    }

    public VideoRequest setPeriod(Period period) {
        this.period = period;
        return this;
    }

    public VideoRequest setSort(VideoSort sort) {
        this.sort = sort;
        return this;
    }

    public VideoRequest setType(VideoType type) {
        this.type = type;
        return this;
    }

    @Override
    public Mono<Videos> get() {
        if (before != null) {
            request.queryParam("before", before);
        }

        if (after != null) {
            request.queryParam("after", after);
        }

        if (first != null && (first > 0 && first <= 100)) {
            request.queryParam("first", first);
        }

        if (language != null) {
            request.queryParam("language", language.toLanguageTag());
        }

        if (period != null) {
            request.queryParam("period", period.name().toLowerCase());
        }

        if (sort != null) {
            request.queryParam("sort", sort.name().toLowerCase());
        }

        if (type != null) {
            request.queryParam("type", type.name().toLowerCase());
        }

        return httpClient.exchangeAs(request, Videos.class);
    }
}