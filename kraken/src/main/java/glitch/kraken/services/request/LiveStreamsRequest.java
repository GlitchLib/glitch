package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.HttpMethod;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.enums.StreamType;
import glitch.kraken.object.json.Channel;
import glitch.kraken.object.json.Game;
import glitch.kraken.object.json.Stream;
import glitch.kraken.object.json.list.Streams;
import glitch.service.AbstractRestService;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Setter
@Accessors(fluent = true, chain = true)
public class LiveStreamsRequest extends AbstractRestService.AbstractRequest<Streams, Stream> {
    private final Set<Channel> channel = new LinkedHashSet<>();
    private Game game;
    private Locale langauge;
    private StreamType streamType;
    private Integer limit;
    private Integer offset;

    public LiveStreamsRequest(HttpClient http) {
        super(http, http.create(HttpMethod.GET, "/streams", Streams.class));
    }

    public LiveStreamsRequest channel(Channel... languages) {
        return channel(Arrays.asList(languages));
    }

    public LiveStreamsRequest channel(Collection<Channel> languages) {
        this.channel.addAll(languages);
        return this;
    }

    @Override
    protected HttpResponse<Streams> exchange() {
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

        return httpClient.exchange(request);
    }

    @Override
    public Mono<Streams> get() {
        return exchange().toMono();
    }

    @Override
    public Flux<Stream> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
