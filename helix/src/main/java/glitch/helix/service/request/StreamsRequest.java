package glitch.helix.service.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.exceptions.http.RequestException;
import glitch.helix.object.json.Stream;
import glitch.helix.object.json.Streams;
import glitch.service.AbstractRequest;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

public class StreamsRequest extends AbstractRequest<Stream, Streams> {
    private Long first;
    private String before;
    private String after;

    private String[] communityId;

    public StreamsRequest(HttpClient http) {
        super(http, Routes.get("/streams").newRequest());
    }

    @Nonnull
    @Override
    public Mono<Streams> get() {
        return Mono.error(new RequestException(new UnsupportedOperationException("Streams is not supported yet!")));
    }
}
