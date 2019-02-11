package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.kraken.object.enums.ClipPeriod;
import glitch.kraken.object.json.Channel;
import glitch.kraken.object.json.Clip;
import glitch.kraken.object.json.Game;
import glitch.kraken.object.json.collections.Clips;
import glitch.service.AbstractRequest;
import java.util.*;
import java.util.stream.Collectors;
import reactor.core.publisher.Mono;

public class TopClipsRequest extends AbstractRequest<Clip, Clips> {
    private final Set<Locale> language = new LinkedHashSet<>(28);
    private Channel channel;
    private Game game;
    private String cursor;
    private Long limit;
    private ClipPeriod period;
    private Boolean trending;

    public TopClipsRequest(HttpClient httpClient) {
        super(httpClient, Routes.get("/clips/top").newRequest());
    }

    public TopClipsRequest addLanguage(Locale... language) {
        return addLanguage(Arrays.asList(language));
    }

    public TopClipsRequest addLanguage(String... language) {
        return addLanguage(Arrays.stream(language).map(Locale::forLanguageTag).collect(Collectors.toSet()));
    }

    public TopClipsRequest addLanguage(Collection<Locale> language) {
        this.language.addAll(language);
        return this;
    }

    public TopClipsRequest setChannel(Channel channel) {
        this.channel = channel;
        return this;
    }

    public TopClipsRequest setGame(Game game) {
        this.game = game;
        return this;
    }

    public TopClipsRequest setCursor(String cursor) {
        this.cursor = cursor;
        return this;
    }

    public TopClipsRequest setLimit(Long limit) {
        this.limit = limit;
        return this;
    }

    public TopClipsRequest setPeriod(ClipPeriod period) {
        this.period = period;
        return this;
    }

    public TopClipsRequest setTrending(Boolean trending) {
        this.trending = trending;
        return this;
    }

    @Override
    public Mono<Clips> get() {

        if (channel != null) {
            request.queryParam("channel", channel.getUsername());
        } else if (game != null) {
            request.queryParam("game", game.getName());
        }

        if (cursor != null) {
            request.queryParam("cursor", cursor);
        }

        if (!language.isEmpty() && language.size() <= 28) {
            request.queryParam("language", language.stream().map(Locale::getLanguage).collect(Collectors.joining(",")));
        }

        if (limit != null && limit > -1 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (period != null) {
            request.queryParam("period", period.name().toLowerCase());
        }

        if (trending != null) {
            request.queryParam("trending", trending);
        }

        return httpClient.exchangeAs(request, Clips.class);
    }
}
