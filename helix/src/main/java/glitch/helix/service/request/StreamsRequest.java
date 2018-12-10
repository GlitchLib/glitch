package glitch.helix.service.request;

import glitch.api.AbstractRequest;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpMethod;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.exceptions.GlitchException;
import glitch.helix.object.json.Stream;
import glitch.helix.object.json.list.Streams;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class StreamsRequest extends AbstractRequest<Streams, Stream> {
    private Long first;
    private String before;
    private String after;

    private String[] communityId;

    public StreamsRequest(GlitchHttpClient http) {
        super(http, http.create(HttpMethod.GET, "/streams", Streams.class));
    }

    @Override
    protected HttpResponse<Streams> exchange() {
        return null;
    }

    @Override
    public Mono<Streams> get() {
        if (before != null && after != null) {
            return Mono.error(new GlitchException("You must define one of pagination's word. \"#before()\" or \"#after()\""));
        } else {
            return exchange().toMono();
        }
    }

    @Override
    public Flux<Stream> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
