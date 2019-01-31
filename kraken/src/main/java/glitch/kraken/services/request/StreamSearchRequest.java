package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.kraken.object.json.Stream;
import glitch.kraken.object.json.collections.Streams;
import glitch.service.AbstractRequest;
import reactor.core.publisher.Mono;

public class StreamSearchRequest extends AbstractRequest<Stream, Streams> {
    private Integer limit;
    private Integer offset;
    private Boolean hls;

    public StreamSearchRequest(HttpClient http, String query) {
        super(http, Routes.get("/search/channels").newRequest().queryParam("query", encodeQuery(query)));
    }

    public StreamSearchRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public StreamSearchRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public StreamSearchRequest setHls(Boolean hls) {
        this.hls = hls;
        return this;
    }

    @Override
    public Mono<Streams> get() {

        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        if (hls != null) {
            request.queryParam("hls", hls);
        }

        return httpClient.exchangeAs(request, Streams.class);
    }
}
