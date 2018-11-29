package glitch.kraken.services.request;

import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.enums.ClipPeriod;
import glitch.kraken.object.json.Channel;
import glitch.kraken.object.json.Clip;
import glitch.kraken.object.json.Game;
import glitch.kraken.object.json.list.Clips;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Setter
@Accessors(chain = true, fluent = true)
public class TopClipsRequest extends AbstractRequest<Clips, Clip> {
    private Channel channel;
    private Game game;
    private String cursor;
    private final Set<Locale> language = new LinkedHashSet<>(28);
    private Long limit;
    private ClipPeriod period;
    private Boolean trending;

    public TopClipsRequest(GlitchHttpClient httpClient, HttpRequest<Clips> request) {
        super(httpClient, request);
    }

    public TopClipsRequest language(Locale... language) {
        return language(Arrays.asList(language));
    }

    public TopClipsRequest language(String... language) {
        return language(Arrays.stream(language).map(Locale::forLanguageTag).collect(Collectors.toSet()));
    }

    public TopClipsRequest language(Collection<Locale> language) {
        this.language.addAll(language);
        return this;
    }

    @Override
    protected HttpResponse<Clips> exchange() {
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

        return httpClient.exchange(request);
    }

    @Override
    public Mono<Clips> get() {
        return exchange().toMono();
    }

    @Override
    public Flux<Clip> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
