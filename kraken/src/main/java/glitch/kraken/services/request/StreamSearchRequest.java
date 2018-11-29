package glitch.kraken.services.request;

import com.google.common.net.UrlEscapers;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpMethod;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Stream;
import glitch.kraken.object.json.list.Streams;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class StreamSearchRequest extends AbstractRequest<Streams, Stream> {
    private Integer limit;
    private Integer offset;
    private Boolean hls;

    public StreamSearchRequest(GlitchHttpClient http, String query) {
        super(http, http.create(HttpMethod.GET, "/search/channels", Streams.class).queryParam("query", UrlEscapers.urlFormParameterEscaper().escape(query)));
    }

    @Override
    protected HttpResponse<Streams> exchange() {
        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        if (hls != null) {
            request.queryParam("hls", hls);
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
