package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.kraken.object.enums.StreamType;
import glitch.kraken.object.json.Channel;
import glitch.kraken.object.json.Game;
import glitch.kraken.object.json.Stream;
import glitch.kraken.object.json.collections.Streams;
import glitch.service.AbstractRequest;
import java.util.*;
import java.util.stream.Collectors;
import reactor.core.publisher.Mono;

public class LiveStreamsRequest extends AbstractRequest<Stream, Streams> {
    private final Set<Channel> channel = new LinkedHashSet<>();
    private Game game;
    private Locale langauge;
    private StreamType streamType;
    private Integer limit;
    private Integer offset;

    public LiveStreamsRequest(HttpClient http) {
        super(http, Routes.get("/streams").newRequest());
    }

    public LiveStreamsRequest setGame(Game game) {
        this.game = game;
        return this;
    }

    public LiveStreamsRequest setLangauge(Locale langauge) {
        this.langauge = langauge;
        return this;
    }

    public LiveStreamsRequest setStreamType(StreamType streamType) {
        this.streamType = streamType;
        return this;
    }

    public LiveStreamsRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public LiveStreamsRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public LiveStreamsRequest addChannel(Channel... languages) {
        return addChannel(Arrays.asList(languages));
    }

    public LiveStreamsRequest addChannel(Collection<Channel> languages) {
        this.channel.addAll(languages);
        return this;
    }

    @Override
    public Mono<Streams> get() {

        if (!channel.isEmpty()) {
            request.queryParam("channel", channel.stream().map(Channel::getId).map(String::valueOf).collect(Collectors.joining(",")));
        }

        if (game != null) {
            request.queryParam("game", game.getName());
        }

        if (langauge != null) {
            request.queryParam("language", langauge.getLanguage());
        }

        if (streamType != null) {
            request.queryParam("stream_type", streamType.name().toLowerCase());
        }

        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        return httpClient.exchangeAs(request, Streams.class);
    }
}
