package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.kraken.object.json.FeatureStream;
import glitch.kraken.object.json.collections.FeatureStreams;
import glitch.service.AbstractRequest;
import reactor.core.publisher.Mono;

public class FeatureStreamsRequest extends AbstractRequest<FeatureStream, FeatureStreams> {
    private Integer limit;
    private Integer offset;

    public FeatureStreamsRequest(HttpClient http) {
        super(http, Routes.get("/streams/featured").newRequest());
    }

    public FeatureStreamsRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public FeatureStreamsRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public Mono<FeatureStreams> get() {
        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        return httpClient.exchangeAs(request, FeatureStreams.class);
    }
}
