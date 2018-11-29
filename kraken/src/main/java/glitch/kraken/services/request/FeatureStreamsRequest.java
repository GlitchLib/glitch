package glitch.kraken.services.request;

import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpMethod;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.FeatureStream;
import glitch.kraken.object.json.list.FeatureStreams;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class FeatureStreamsRequest extends AbstractRequest<FeatureStreams, FeatureStream> {
    private Integer limit;
    private Integer offset;

    public FeatureStreamsRequest(GlitchHttpClient http) {
        super(http, http.create(HttpMethod.GET, "/streams/featured", FeatureStreams.class));
    }

    @Override
    protected HttpResponse<FeatureStreams> exchange() {
        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        return httpClient.exchange(request);
    }

    @Override
    public Mono<FeatureStreams> get() {
        return exchange().toMono();
    }

    @Override
    public Flux<FeatureStream> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
