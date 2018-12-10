package glitch.helix.service.request;

import glitch.api.AbstractRequest;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpMethod;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.exceptions.GlitchException;
import glitch.helix.object.json.Follow;
import glitch.helix.object.json.list.Follows;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class UserFollowRequest extends AbstractRequest<Follows, Follow> {
    private String after;
    private Integer first;
    private final Long fromId;
    private final Long toId;

    public UserFollowRequest(GlitchHttpClient httpClient, Long fromId, Long toId) {
        super(httpClient, httpClient.create(HttpMethod.GET, "/users/follows", Follows.class));
        this.fromId = fromId;
        this.toId = toId;
    }

    @Override
    protected HttpResponse<Follows> exchange() {
        if (after != null) {
            request.queryParam("after", after);
        }

        if (first != null && first > 0 && first <= 100) {
            request.queryParam("first", first);
        }

        if (fromId != null) {
            request.queryParam("from_id", fromId);
        }

        if (toId != null) {
            request.queryParam("to_id", toId);
        }

        return httpClient.exchange(request);
    }

    @Override
    public Mono<Follows> get() {
        if (fromId != null && toId != null) {
            return Mono.error(new GlitchException("Cannot obtain from booth side user follow"));
        } else {
            return exchange().toMono();
        }
    }

    @Override
    public Flux<Follow> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
