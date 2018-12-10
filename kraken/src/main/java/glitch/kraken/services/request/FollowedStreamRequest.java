package glitch.kraken.services.request;

import glitch.api.AbstractRequest;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpMethod;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.auth.Scope;
import glitch.auth.objects.json.Credential;
import glitch.kraken.object.enums.StreamType;
import glitch.kraken.object.json.Stream;
import glitch.kraken.object.json.list.Streams;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class FollowedStreamRequest extends AbstractRequest<Streams, Stream> {
    private final Credential credential;

    private StreamType streamType;
    private Integer limit;
    private Integer offset;

    public FollowedStreamRequest(GlitchHttpClient http, Credential credential) {
        super(http, http.create(HttpMethod.GET, "/streams/followed", Streams.class));
        this.credential = credential;
    }

    @Override
    protected HttpResponse<Streams> exchange() {

        if (streamType != null) {
            request.queryParam("stream_type", streamType.name().toLowerCase());
        }

        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        request.header("Authroization", "OAuth " + credential.getAccessToken());

        return httpClient.exchange(request);
    }

    @Override
    public Mono<Streams> get() {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.USER_READ)).flatMap(b -> {
            if (b) {
                return exchange().toMono();
            } else {
                return Mono.error(handleScopeMissing(Scope.USER_READ));
            }
        });
    }

    @Override
    public Flux<Stream> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
