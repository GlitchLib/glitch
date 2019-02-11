package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Credential;
import glitch.kraken.object.enums.StreamType;
import glitch.kraken.object.json.Stream;
import glitch.kraken.object.json.collections.Streams;
import glitch.service.AbstractRequest;
import reactor.core.publisher.Mono;

public class FollowedStreamRequest extends AbstractRequest<Stream, Streams> {
    private final Credential credential;

    private StreamType streamType;
    private Integer limit;
    private Integer offset;

    public FollowedStreamRequest(HttpClient http, Credential credential) {
        super(http, Routes.get("/streams/followed").newRequest());
        this.credential = credential;
    }

    public FollowedStreamRequest setStreamType(StreamType streamType) {
        this.streamType = streamType;
        return this;
    }

    public FollowedStreamRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public FollowedStreamRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public Mono<Streams> get() {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.USER_READ)).flatMap(b -> {
            if (b) {
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

                return httpClient.exchangeAs(request, Streams.class);
            } else {
                return Mono.error(handleScopeMissing(GlitchScope.USER_READ));
            }
        });
    }
}
